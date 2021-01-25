package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.CheckboxesAdapter;
import Adapters.GroupSpinnerAdapter;
import Room.DebtSet;
import Room.DebtSetStatuses;
import Room.Group;
import Room.GroupWithPeople;
import Room.Person;
import ViewModel.MainActivityViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements CheckboxesAdapter.OnPeopleCheckboxesListener {

    Context context;
    TextView mDebtAmount;
    EditText mDescription;
    RecyclerView mCheckboxesRecyclerView;
    ImageButton mNumericBackspaceImageButton;
    TextView mCreditorTextView;
    Spinner mGroupSpinner;
    Group selectedGroup;

    List<Person> peopleArraylist = new ArrayList<>();
    List<Group> groups = new ArrayList<>();

    private MainActivityViewModel mainActivityViewModel;

    public static final int ADD_PERSON_REQUEST = 1;
    public static final int EDIT_CREDITOR_REQUEST = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        Log.d(TAG, "onCreate: start");
        context = getApplicationContext();
        mCheckboxesRecyclerView = findViewById(R.id.checkboxes);
        mDebtAmount = findViewById(R.id.debt_amount);
        mDescription = findViewById(R.id.debt_desc);
        mCreditorTextView = findViewById(R.id.creditor_textView);
        mGroupSpinner = findViewById(R.id.Group_Spinner);
        GroupSpinnerAdapter spinnerAdapter = new GroupSpinnerAdapter(this, groups);
        mGroupSpinner.setAdapter(spinnerAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (Group group :
                        groups) {
                    if (group.isActive()) group.setActive(false);
                    mainActivityViewModel.update(group);
                }
                selectedGroup = (Group) adapterView.getItemAtPosition(i);
                selectedGroup.setActive(true);
                mainActivityViewModel.update(selectedGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mNumericBackspaceImageButton = findViewById(R.id.numeric_backspace_image_button);
        mNumericBackspaceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String debtAmountString = mDebtAmount.getText().toString();
                if (mDebtAmount.getText().toString().length() > 1) {
                    String changed = debtAmountString.substring(0, debtAmountString.length() - 1);
                    mDebtAmount.setText(changed);
                } else {
                    mDebtAmount.setText("0");
                }
                updateCurrentValueOnDebtors();
            }
        });


        mCheckboxesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mCheckboxesRecyclerView.setHasFixedSize(true);


        final CheckboxesAdapter checkboxesAdapter = new CheckboxesAdapter(this);
        mCheckboxesRecyclerView.setAdapter(checkboxesAdapter);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getActiveGroupWithPeople().observe(this, new Observer<GroupWithPeople>() {
            @Override
            public void onChanged(GroupWithPeople groupWithPeople) {
                checkboxesAdapter.setPeople(groupWithPeople.peopleInGroup);
                setPeopleArraylist(groupWithPeople.peopleInGroup);
                checkPeopleOnCreditor();
            }
        });

        mainActivityViewModel.getAllGroups().observe(this, new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                setGroups(groups);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_item:
                //TODO zaimplementuj usówanie grupy
                //groupViewModel.delete();
                return true;
            case R.id.add_new_person_item:
                Intent intent = new Intent(MainActivity.this, AddEditPersonActivity.class);
                startActivityForResult(intent, ADD_PERSON_REQUEST);
                return true;
            case R.id.show_list_of_persons_item:
                Intent intent2 = new Intent(MainActivity.this, PeopleListActivity.class);
                startActivity(intent2);
                return true;
            case R.id.clear_all_debts_and_data:
                clearAlldebtsAndData();
                return true;
            case R.id.resolve_all_debts_item:
                //TODO zaimplementuj sprawdzenie dostępności długów do rozliczenia

                Intent intent3 = new Intent(MainActivity.this, MoneyFlowActivity.class);
                startActivity(intent3);
                return true;
            case R.id.show_history:
                Intent intent4 = new Intent(this, HistoryActivity.class);
                startActivity(intent4);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCurrentValueOnDebtors() {
        ArrayList<Person> debtors = new ArrayList<>();
        for (Person person : peopleArraylist) {
            if (person.isCurrentDebtor()) debtors.add(person);
        }
        int currentValue = Integer.valueOf(mDebtAmount.getText().toString());
        if (currentValue != 0 && debtors.size() != 0) {
            if (debtors.size() != 1) {
                if (currentValue % 2 == 0) currentValue = currentValue / debtors.size();
                else currentValue = currentValue / debtors.size() + 1;
            }
        } else currentValue = 0;
        for (Person debtor : debtors) {
            debtor.setCurrentValue(currentValue);
            mainActivityViewModel.update(debtor);
        }
    }


    @Override
    public void onPersonCheckboxClick(Person person) {
        if (person.isCurrentDebtor()) {
            person.setCurrentDebtor(false);
            mainActivityViewModel.update(person);
            updateCurrentValueOnDebtors();
        } else {
            person.setCurrentDebtor(true);
            mainActivityViewModel.update(person);
            updateCurrentValueOnDebtors();
        }
    }


    public void submitDebt(View view) {

        Integer value = Integer.valueOf(mDebtAmount.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("pl", "PL"));
        if (value <= 0) {
            Toast.makeText(this, "debt can't be 0 or less", Toast.LENGTH_LONG).show();
            mDebtAmount.setText("0");
            return;
        }

        Person currentCreditor = null;
        ArrayList<Person> currentDebtors = new ArrayList<>();
        for (Person person : peopleArraylist) {
            if (person.isCurrentCreditor()) currentCreditor = person;
            if (person.isCurrentDebtor()) currentDebtors.add(person);
        }

        if (currentCreditor == null || currentDebtors.isEmpty()) {
            Toast.makeText(this, "have to choose at least one creditor and one debtor", Toast.LENGTH_LONG).show();
            return;
        }

        Date currentDate = new Date();

        DebtSet debtSet = new DebtSet();
        for (Person debtor : currentDebtors) {
            debtSet.setDebtorId(debtor.getId());
            debtSet.setValue(debtor.getCurrentValue());
            debtSet.setCreditorId(currentCreditor.getId());
            debtSet.setStatus(DebtSetStatuses.Unresolved);
            debtSet.setGroupId(selectedGroup.getGroupId());
            if (mDescription.getText() != null) {
                debtSet.setDescription(mDescription.getText().toString());
            }
            debtSet.setDate(currentDate);

            debtor.setCurrentValue(0);
            debtor.setCurrentDebtor(false);
            mainActivityViewModel.update(debtor);
        }
        mainActivityViewModel.update(currentCreditor);
        mainActivityViewModel.insert(debtSet);
        mDebtAmount.setText("0");
        mDescription.setText("");
        Log.d(TAG, "submitDebt: date: " + sdf.format(currentDate));
    }


    private void clearAlldebtsAndData() {
        for (Person person : peopleArraylist) {
            //TODO zaimplementój czyszczenie
            person.setBalance(0);

        }
    }


    public void editCreditor(View view) {
        Log.d(TAG, "editCreditor: ");
        for (Person person : peopleArraylist) {
            if (person.isCurrentCreditor()) person.setCurrentCreditor(false);
            mainActivityViewModel.update(person);
        }

        Intent intent = new Intent(getApplicationContext(), ChooseCreditorActivity.class);
        startActivityForResult(intent, EDIT_CREDITOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST) {
            if (resultCode == RESULT_OK) {
                String name = null;
                if (data != null) {
                    name = data.getStringExtra(AddEditPersonActivity.EXTRA_NAME);
                }
                for (Person person : peopleArraylist) {
                    if (person.getName().equals(name)) {
                        Toast.makeText(this, "names can't repeat", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Person person = new Person(name);
                mainActivityViewModel.insert(person);
                Log.d(TAG, "onActivityResult: personInserted");
            } else {
                Log.d(TAG, "onActivityResult: personNOTinserted");
            }
            return;
        }
        if (requestCode == EDIT_CREDITOR_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "setCreditor onActivityResult: EDIT_CREDITOR_REQUEST");
                int id = data.getIntExtra(ChooseCreditorActivity.CURRENT_CREDITOR, 0);
                Log.d(TAG, "setCreditor onActivityResult: EDIT_CREDITOR_REQUEST, id: " + id);
                for (Person person : peopleArraylist) {
                    if (person.getId() == id) {
                        Log.d(TAG, "setCreditor onActivityResult: SET_CREDITOR: " + person.getId());
                        person.setCurrentCreditor(true);
                        mainActivityViewModel.update(person);
                    }


                }
            }

        }

    }

    public void calculate(View view) {
        switch (view.getId()) {
            case R.id.numeric_1_image_button:
                updatemDebtTextView(1);
                break;
            case R.id.numeric_2_image_button:
                updatemDebtTextView(2);
                break;
            case R.id.numeric_3_image_button:
                updatemDebtTextView(3);
                break;
            case R.id.numeric_4_image_button:
                updatemDebtTextView(4);
                break;
            case R.id.numeric_5_image_button:
                updatemDebtTextView(5);
                break;
            case R.id.numeric_6_image_button:
                updatemDebtTextView(6);
                break;
            case R.id.numeric_7_image_button:
                updatemDebtTextView(7);
                break;
            case R.id.numeric_8_image_button:
                updatemDebtTextView(8);
                break;
            case R.id.numeric_9_image_button:
                updatemDebtTextView(9);
                break;
            case R.id.numeric_0_image_button:
                updatemDebtTextView(0);
                break;
        }
    }

    private void updatemDebtTextView(Integer i) {
        if (Integer.valueOf(mDebtAmount.getText().toString()) == 0)
            mDebtAmount.setText(i.toString());
        else {
            if (mDebtAmount.getText().length() == 8) {
                Toast.makeText(context, "nie masz tyle kasy nawet ;)", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder sb = new StringBuilder();
            String debtAmountString = mDebtAmount.getText().toString();
            sb.append(debtAmountString);
            sb.append(String.valueOf(i));
            mDebtAmount.setText(sb.toString());
        }
        updateCurrentValueOnDebtors();
    }


    private void checkPeopleOnCreditor() {
        boolean creditorSet = false;
        for (Person person : peopleArraylist) {
            if (person.isCurrentCreditor()) {
                mCreditorTextView.setText(person.getName());
                creditorSet = true;
            }
            if (!creditorSet) {
                mCreditorTextView.setText("no creditor");
            }
        }
    }

    public void setPeopleArraylist(List<Person> peopleArraylist) {
        this.peopleArraylist = peopleArraylist;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
