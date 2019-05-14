package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import Adapters.CheckboxesAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
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
    ImageButton mSelectAllButton;
    ImageButton mNumericBackspaceImageButton;
    TextView mCreditorTextView;
    Person currentCreditor;

    List<Person> peopleArraylist = new ArrayList<>();

    ArrayList<Person> currentDebtors = new ArrayList<>();

    private PersonViewModel personViewModel;
    public static final int ADD_PERSON_REQUEST = 1;
    public static final int EDIT_CREDITOR_REQUEST = 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAGC = "setCurrentCreditor";
    public static final String DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY = MainActivity.class.getSimpleName() + "global_people_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: start");

        context = getApplicationContext();
        mCheckboxesRecyclerView = findViewById(R.id.checkboxes);
        mDebtAmount = findViewById(R.id.debt_amount);
        mSelectAllButton = findViewById(R.id.select_all_button);
        mSelectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Person person : peopleArraylist) {
                    boolean isCurrentDebtor = false;
                    for (Person debtor : currentDebtors) {
                        if (person.equals(debtor)) {
                            isCurrentDebtor = true;
                        }
                    }
                    if (!isCurrentDebtor) {
                        currentDebtors.add(person);
                        mCheckboxesRecyclerView.findViewById(person.getId()).setBackgroundColor(getResources().getColor(R.color.green));
                    }
                }
            }
        });
        mDescription = findViewById(R.id.debt_desc);
        mCreditorTextView = findViewById(R.id.creditor_textView);
        mNumericBackspaceImageButton = findViewById(R.id.numeric_backspace_image_button);
        mNumericBackspaceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String debtAmountString = mDebtAmount.getText().toString();
                mDebtAmount.setText(debtAmountString.substring(0, debtAmountString.length() - 1));
            }
        });


        mCheckboxesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mCheckboxesRecyclerView.setHasFixedSize(true);


        final CheckboxesAdapter checkboxesAdapter = new CheckboxesAdapter(this);
        mCheckboxesRecyclerView.setAdapter(checkboxesAdapter);


        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                checkboxesAdapter.setPeople(people);
                setPeopleArraylist(people);
                boolean isCreditorDeleted = true;
                for (Person person : peopleArraylist){
                    if (person.equals(currentCreditor)) isCreditorDeleted = false;
                }
                if (isCreditorDeleted) {
                    currentCreditor = null;
                    mCreditorTextView.setText("no creditor");
                }
                Log.d(TAG, "personViewModel -> observer -> onChanged");
            }
        });


    }//onCreate

    @Override
    public void onPersonCheckboxClick(Person person) {
        int id = person.getId();
        boolean currentDebtor = false;
        for (Person debtor : currentDebtors) {
            if (person.equals(debtor)) currentDebtor = true;
        }
        if (!currentDebtor) {
            currentDebtors.add(person);
            mCheckboxesRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.green));

        } else {
            currentDebtors.remove(person);
            mCheckboxesRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.white));
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
                currentDebtors = new ArrayList<>();
                Intent intent2 = new Intent(MainActivity.this, PeopleListActivity.class);
                startActivity(intent2);
                return true;
            case R.id.clear_all_debts_and_data:
                clearAlldebtsAndData();
                return true;
            case R.id.resolve_all_debts_item:
                Collections.sort(peopleArraylist);
                createMoneyFlows(0, peopleArraylist.size() - 1);
                Intent intent3 = new Intent(MainActivity.this, MoneyFlowActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createMoneyFlows(int creditorIndex, int debtorIndex) {
        Person creditor = peopleArraylist.get(creditorIndex);
        Person debtor = peopleArraylist.get(debtorIndex);
        //TODO do optymalizacji: metoda powinna wyszukiwać takich połączeń w których zachodzi equals, eliminować, po tym robić poniższe
        if (!debtor.isBalanced()) {
            if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance())) > 0) {
                debtor.addMoneyFlow(creditor.getName(), debtor.getBalance(), debtor.getName());
                creditor.setBalance(creditor.getBalance() + debtor.getBalance());
//                debtor.setBalance(0);
                debtor.setBalanced(true);
                debtorIndex--;
                createMoneyFlows(creditorIndex, debtorIndex);
            } else if (creditor.getBalance().equals(Math.abs(debtor.getBalance()))) {
                creditor.setBalance(0);
                creditor.setBalanced(true);
                creditorIndex++;
                debtor.addMoneyFlow(creditor.getName(), debtor.getBalance(), debtor.getName());
//                debtor.setBalance(0);
                debtor.setBalanced(true);
                debtorIndex--;
                createMoneyFlows(creditorIndex, debtorIndex);
            } else if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance())) < 0) {
                debtor.setBalance(debtor.getBalance() + creditor.getBalance());
                debtor.addMoneyFlow(creditor.getName(), creditor.getBalance(), debtor.getName());
                creditor.setBalanced(true);
//                creditor.setBalance(0);
                creditorIndex++;
                createMoneyFlows(creditorIndex, debtorIndex);
            }
        }
        personViewModel.update(creditor);
        personViewModel.update(debtor);
    }

    private void clearAlldebtsAndData() {
        for (Person person : peopleArraylist) {
            person.setBalance(0);
            person.setBalanced(false);
            person.setDebtSets(new ArrayList<DebtSet>());
            person.setMoneyFlow(new ArrayList<DebtSet>());
            personViewModel.update(person);
        }
    }


    public void submitDebt(View view) {
        Integer value = Integer.valueOf(mDebtAmount.getText().toString());
        if (value <= 0) {
            Toast.makeText(this, "debt can't be 0 or less", Toast.LENGTH_LONG).show();
            return;
        }
        if (currentCreditor == null || currentDebtors.isEmpty()) {
            Toast.makeText(this, "have to choose at least one creditor and one debtor", Toast.LENGTH_LONG).show();
            return;
        }
        for (Person debtor : currentDebtors) {
            DebtSet debtSet = new DebtSet(currentCreditor.getName(), value, debtor.getName());
            if (mDescription.getText() != null)
                debtSet.setDescription(mDescription.getText().toString());
            debtSet.setDate(Calendar.getInstance().getTime());
            currentCreditor.addDebt(debtSet);
            debtor.setBalance(debtor.getBalance() - value);
            currentCreditor.setBalance(currentCreditor.getBalance() + value);
            personViewModel.update(debtor);
            mCheckboxesRecyclerView.findViewById(debtor.getId()).setBackgroundColor(getResources().getColor(R.color.white));
        }
        personViewModel.update(currentCreditor);
        currentDebtors = new ArrayList<>();
        mDebtAmount.setText("0");

    }

    public void editCreditor(View view) {
        Log.d(TAGC, "editCreditor: ");
        currentCreditor = null;
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
                        Log.d(TAG, "setCreditor onActivityResult: SET_CREDITOR: "+person.getName());
                        currentCreditor = person;
                        mCreditorTextView.setText(name);
                        return;
                    }


                }
            }

        }

    }


    public void showDebts(View view) {
        //TODO zapisz global map do bundle i InstateState
        Intent intent = new Intent(this, HistoryActivity.class);
        Log.d(TAG, "showDebts: starting HistoryActivity");
        startActivity(intent);
    }


    public void setPeopleArraylist(List<Person> peopleArraylist) {
        this.peopleArraylist = peopleArraylist;
    }


    public void calculate(View view) {
        switch (view.getId()) {
            case R.id.numeric_1_image_button:
                addNumeric(1);
                break;
            case R.id.numeric_2_image_button:
                addNumeric(2);
                break;
            case R.id.numeric_3_image_button:
                addNumeric(3);
                break;
            case R.id.numeric_4_image_button:
                addNumeric(4);
                break;
            case R.id.numeric_5_image_button:
                addNumeric(5);
                break;
            case R.id.numeric_6_image_button:
                addNumeric(6);
                break;
            case R.id.numeric_7_image_button:
                addNumeric(7);
                break;
            case R.id.numeric_8_image_button:
                addNumeric(8);
                break;
            case R.id.numeric_9_image_button:
                addNumeric(9);
                break;
            case R.id.numeric_0_image_button:
                addNumeric(0);
                break;
        }
    }

    private void addNumeric(Integer i) {
        if (Integer.valueOf(mDebtAmount.getText().toString()) == 0)
            mDebtAmount.setText(i.toString());
        else {
            StringBuilder sb = new StringBuilder();
            String debtAmountString = mDebtAmount.getText().toString();
            sb.append(debtAmountString);
            sb.append(String.valueOf(i));
            mDebtAmount.setText(sb.toString());
        }
    }
}
