package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.CheckboxesAdapter;
import Room.GroupWithPeople;
import Room.Person;
import ViewModel.DebtSetViewModel;
import ViewModel.GroupViewModel;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class MainActivity extends AppCompatActivity implements CheckboxesAdapter.OnPeopleCheckboxesListener {

    Context context;
    TextView mDebtAmount;
    EditText mDescription;
    RecyclerView mCheckboxesRecyclerView;
    ImageButton mNumericBackspaceImageButton;
    TextView mCreditorTextView;

    List<Person> peopleArraylist = new ArrayList<>();
    private PersonViewModel personViewModel;
    private GroupViewModel groupViewModel;
    private DebtSetViewModel debtSetViewModel;
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

        //TODO pobierz osoby z grupy
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        groupViewModel.getActiveGroupWithPeople().observe(this, new Observer<GroupWithPeople>() {
            @Override
            public void onChanged(GroupWithPeople groupWithPeople) {
                checkboxesAdapter.setPeople(groupWithPeople.peopleInGroup);
                setPeopleArraylist(groupWithPeople.peopleInGroup);
                checkPeopleOnCreditor();
            }
        });
        /*personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                checkboxesAdapter.setPeople(people);
                setPeopleArraylist(people);
                Log.d(TAG, "createMoneyFlows_1stStep: peopleArraylistChanged");
                Log.d(TAG, "personViewModel -> observer -> onChanged");
                checkPeopleOnCreditor();
            }
        });*/

    }//onCreate

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
            personViewModel.update(debtor);
        }
    }


    @Override
    public void onPersonCheckboxClick(Person person) {
        if (person.isCurrentDebtor()) {
            person.setCurrentDebtor(false);
            personViewModel.update(person);
            updateCurrentValueOnDebtors();
        } else {
            person.setCurrentDebtor(true);
            personViewModel.update(person);
            updateCurrentValueOnDebtors();
        }
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
                personViewModel.deleteAll();
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
                if (peopleArraylist.size() == 0) {
                    Toast.makeText(context, "nie ma długów do rozliczenia", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Collections.sort(peopleArraylist);
                for (Person person : peopleArraylist) {
                    person.setTempBalance(person.getBalance());
                    person.setBalanced(false);
                    person.setMoneyFlow(new ArrayList<DebtSet>());
                    if (person.getTempBalance() == 0) person.setBalanced(true);
                }
                createMoneyFlows(0, peopleArraylist.size() - 1);
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

        for (Person debtor : currentDebtors) {

            Integer splitedValue;

            if (currentDebtors.size() == 1) {
                splitedValue = value;
            } else {
                splitedValue = value / currentDebtors.size();
                if (value % currentDebtors.size() != 0) {
                    splitedValue = value / currentDebtors.size() + 1;
                }
            }

            DebtSet debtSet = new DebtSet(currentCreditor.getName(), splitedValue, debtor.getName());
            if (mDescription.getText() != null)
                debtSet.setDescription(mDescription.getText().toString());
            Date currentDate = new Date();
            debtSet.setDate(new Date());
            if (currentCreditor.isActive() && debtor.isActive()) {
                currentCreditor.addDebt(debtSet);
                debtor.setBalance(debtor.getBalance() - splitedValue);
                debtor.setTempBalance(debtor.getBalance());
                currentCreditor.setBalance(currentCreditor.getBalance() + splitedValue);
                currentCreditor.setTempBalance(currentCreditor.getBalance());
                debtor.setCurrentDebtor(false);
            } else if (!currentCreditor.isActive() || !debtor.isActive()) {
                debtSet.setActive(false);
                currentCreditor.addDebt(debtSet);
                debtor.setCurrentDebtor(false);
            }

            Log.d(TAG, "submitDebt: date: " + sdf.format(currentDate));
            personViewModel.update(debtor);
        }

        personViewModel.update(currentCreditor);
        mDebtAmount.setText("0");
        mDescription.setText("");

    }

    private void createMoneyFlows(int creditorIndex, int debtorIndex) {
        createMoneyFlows_1stStep();
        createMoneyFlows_2ndStep(creditorIndex, debtorIndex);

    }

    private void createMoneyFlows_1stStep() {
        for (Person person : peopleArraylist) {
            if (!person.isBalanced()) {
                Person creditor = person;
                Integer creditorBalance = person.getTempBalance();
                for (int i = peopleArraylist.size() - 1; i >= 0; i--) {
                    Person debtor = peopleArraylist.get(i);
                    Integer debtorBalance = debtor.getTempBalance();
                    if (debtorBalance.compareTo(0) < 0) {
                        if (creditorBalance.compareTo(Math.abs(debtorBalance)) == 0) {
                            debtor.addMoneyFlow(creditor.getName(), creditorBalance, debtor.getName());
                            creditor.setBalanced(true);
                            creditor.setTempBalance(0);
                            debtor.setTempBalance(0);
                            debtor.setBalanced(true);
                            personViewModel.update(creditor);
                            personViewModel.update(debtor);
                            Log.d(TAG, "createMoneyFlows_1stStep: debtor.addMoneyFlow(" + creditor.getName() + ", " + creditorBalance + ", " + debtor.getName() + ");");
                            createMoneyFlows_1stStep();
                        }
                    }
                }
            }

        }
    }

    private void createMoneyFlows_2ndStep(int creditorIndex, int debtorIndex) {
        Collections.sort(peopleArraylist);

        boolean isEveryoneBalanced = true;
        for (Person person : peopleArraylist) {
            if (!person.isBalanced()) isEveryoneBalanced = false;
        }
        if (isEveryoneBalanced) return;

        int endIndex = peopleArraylist.size() - 1;

        Person creditor = peopleArraylist.get(creditorIndex);
        Person debtor = peopleArraylist.get(debtorIndex);

        if (creditor.isBalanced()) {
            for (int i = creditorIndex; i < peopleArraylist.size(); i++) {
                if (!peopleArraylist.get(i).isBalanced() && peopleArraylist.get(i).getTempBalance().compareTo(0) > 0) {
                    createMoneyFlows_2ndStep(i, debtorIndex);
                }
            }
        }
        if (debtor.isBalanced()) {
            for (int i = debtorIndex; i >= 0; i--) {
                if (!peopleArraylist.get(i).isBalanced() && peopleArraylist.get(i).getTempBalance().compareTo(0) < 0) {
                    createMoneyFlows_2ndStep(creditorIndex, i);
                }
            }
        }

        Integer creditorBalance = creditor.getTempBalance();
        Integer debtorBalance = debtor.getTempBalance();

        if (!(creditor.isBalanced() && debtor.isBalanced())) {
            if (creditorBalance.compareTo(Math.abs(debtorBalance)) > 0) {
                debtor.addMoneyFlow(creditor.getName(), debtorBalance * (-1), debtor.getName());
                Log.d(TAG, "> createMoneyFlows_2ndStep: debtor.addMoneyFlow(" + creditor.getName() + ", " + debtorBalance * (-1) + ", " + debtor.getName() + ");");
                creditor.setTempBalance(creditorBalance + debtorBalance);
                debtor.setBalanced(true);
                personViewModel.update(debtor);
                createMoneyFlows_2ndStep(0, endIndex);
            } else if (creditorBalance.compareTo(Math.abs(debtorBalance)) == 0) {
                debtor.addMoneyFlow(creditor.getName(), creditorBalance, debtor.getName());
                Log.d(TAG, "== createMoneyFlows_2ndStep: debtor.addMoneyFlow(" + creditor.getName() + ", " + creditorBalance + ", " + debtor.getName() + ");");
                debtor.setBalanced(true);
                creditor.setBalanced(true);
                personViewModel.update(debtor);
                createMoneyFlows_2ndStep(0, endIndex);
            } else if (creditorBalance.compareTo(Math.abs(debtorBalance)) < 0) {
                debtor.addMoneyFlow(creditor.getName(), creditorBalance, debtor.getName());
                Log.d(TAG, "< createMoneyFlows_2ndStep: debtor.addMoneyFlow(" + creditor.getName() + ", " + creditorBalance + ", " + debtor.getName() + ");");
                creditor.setBalanced(true);
                debtor.setTempBalance(debtorBalance + creditorBalance);
                personViewModel.update(debtor);
                createMoneyFlows_2ndStep(0, endIndex);
            }
        }
    }

    private void clearAlldebtsAndData() {
        for (Person person : peopleArraylist) {
            person.setBalance(0);
            person.setBalanced(false);
            person.setDebtSets(new ArrayList<DebtSet>());
            person.setMoneyFlow(new ArrayList<DebtSet>());
            person.setActive(true);
            personViewModel.update(person);
        }
    }


    public void editCreditor(View view) {
        Log.d(TAG, "editCreditor: ");
        for (Person person : peopleArraylist) {
            if (person.isCurrentCreditor()) person.setCurrentCreditor(false);
            personViewModel.update(person);
        }

        Intent intent = new Intent(getApplicationContext(), ChooseCreditorActivity.class);
        startActivityForResult(intent, EDIT_CREDITOR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(AddEditPersonActivity.EXTRA_NAME);
                for (Person person : peopleArraylist) {
                    if (person.getName().equals(name)) {
                        Toast.makeText(this, "names can't repeat", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Person person = new Person(name);
                personViewModel.insert(person);
                Log.d(TAG, "onActivityResult: personInserted");
            } else {
                Log.d(TAG, "onActivityResult: personNOTinserted");
            }
            return;
        }
        if (requestCode == EDIT_CREDITOR_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "setCreditor onActivityResult: EDIT_CREDITOR_REQUEST");
                String name = data.getStringExtra(ChooseCreditorActivity.CURRENT_CREDITOR_NAME);
                Log.d(TAG, "setCreditor onActivityResult: EDIT_CREDITOR_REQUEST, name: " + name);
                for (Person person : peopleArraylist) {
                    if (person.getName().equals(name)) {
                        Log.d(TAG, "setCreditor onActivityResult: SET_CREDITOR: " + person.getName());
                        person.setCurrentCreditor(true);
                        personViewModel.update(person);
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
                if (!person.isActive()) {
                    mCreditorTextView.setTextColor(Color.GRAY);
                } else {
                    mCreditorTextView.setTextColor(Color.BLACK);
                }
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
}
