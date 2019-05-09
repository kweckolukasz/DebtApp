package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import Adapters.CheckboxesAdapter;
import Adapters.RadioAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class MainActivity extends AppCompatActivity implements CheckboxesAdapter.OnPeopleCheckboxesListener, RadioAdapter.OnPersonRadioListener {

    Context context;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;
    EditText mDebtAmount;
    RecyclerView mRadioGroupRecyclerView;
    RecyclerView mCheckboxesRecyclerView;
    Button mSelectAllButton;


    List<Person> peopleArraylist = new ArrayList<>();
    Person currentCreditor;
    ArrayList<Person> currentDebtors = new ArrayList<>();

    private PersonViewModel personViewModel;
    private static final String DEBT_AMOUNT_LESSER_THAN_0 = "cannot enter no-positive value, click change direction button to divert the arrow";
    public static final int ADD_PERSON_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY = MainActivity.class.getSimpleName() + "global_people_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: start");

        context = getApplicationContext();
        mRadioGroupRecyclerView = findViewById(R.id.radio_left);
        mCheckboxesRecyclerView = findViewById(R.id.checkboxes);
        mDebtAmount = findViewById(R.id.debt_amount);
        mSelectAllButton = findViewById(R.id.select_all_button);
        mSelectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Person person : peopleArraylist) {
                    boolean isCurrentDebtor = false;
                    for (Person debtor : currentDebtors){
                        if (person.equals(debtor)){
                            isCurrentDebtor = true;
                        }
                    }
                    if (!isCurrentDebtor){
                        currentDebtors.add(person);
                        mCheckboxesRecyclerView.findViewById(person.getId()).setBackgroundColor(getResources().getColor(R.color.green));
                    }
                }
            }
        });

        //keepNoMinusValuesInsideDebtAmountText();

        mCheckboxesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCheckboxesRecyclerView.setHasFixedSize(true);

        mRadioGroupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRadioGroupRecyclerView.setHasFixedSize(true);


        final CheckboxesAdapter checkboxesAdapter = new CheckboxesAdapter(this);
        final RadioAdapter radioAdapter = new RadioAdapter(this);
        mCheckboxesRecyclerView.setAdapter(checkboxesAdapter);
        mRadioGroupRecyclerView.setAdapter(radioAdapter);


        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                checkboxesAdapter.setPeople(people);
                radioAdapter.setPeople(people);
                setPeopleArraylist(people);
                Log.d(TAG, "personViewModel -> observer -> onChanged");
            }
        });

    }

    @Override
    public void onPersonRadioClick(Person person) {
        int id = person.getId();
        if (currentCreditor == null){
            currentCreditor = person;
            mRadioGroupRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.green));
            return;
        }
        if (!person.equals(currentCreditor)) {
            mRadioGroupRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.green));
            currentCreditor = person;
            for (Person person1 : peopleArraylist) {
                if (!person1.equals(currentCreditor)) {
                    mRadioGroupRecyclerView.findViewById(person1.getId()).setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
            return;
        }
        if (person.equals(currentCreditor)){
            currentCreditor = null;
            mRadioGroupRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

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
                Intent intent2 = new Intent(MainActivity.this, PeopleListActivity.class);
                startActivity(intent2);
                return true;
            case R.id.clear_all_debts_and_data:
                clearAlldebtsAndData();
                return true;
            case R.id.resolve_all_debts_item:
                Collections.sort(peopleArraylist);
                createMoneyFlows(0, peopleArraylist.size()-1);
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
            if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance()))>0) {
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
            } else if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance()))<0) {
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
        for (Person person:peopleArraylist){
            person.setBalance(0);
            person.setBalanced(false);
            person.setDebtSets(new ArrayList<DebtSet>());
            person.setMoneyFlow(new ArrayList<DebtSet>());
            personViewModel.update(person);
        }
    }


    public void calculate(View view) {

        amount = Integer.valueOf(mDebtAmount.getText().toString());
        switch (view.getId()) {
            case R.id.add1:
                amountDiff = 1;
                break;
            case R.id.add10:
                amountDiff = 10;
                break;
            case R.id.add100:
                amountDiff = 100;
                break;
            case R.id.substract1:
                amountDiff = -1;
                break;
            case R.id.substract10:
                amountDiff = -10;
                break;
            case R.id.substract100:
                amountDiff = -100;
                break;
        }
        if ((amount + amountDiff) >= 0) {
            amount = Integer.valueOf(mDebtAmount.getText().toString());
            totalAmount = amount + amountDiff;
            mDebtAmount.setText(String.format(Locale.US, "%d", totalAmount));
        }
    }

    public void submitDebt(View view) {
        Integer value = Integer.valueOf(mDebtAmount.getText().toString());
        if (value <= 0) {
            Toast.makeText(this, "debt can't be 0 or less", Toast.LENGTH_LONG).show();
            return;
        }
        if (currentCreditor == null || currentDebtors.isEmpty()){
            Toast.makeText(this, "have to choose at least one creditor and one debtor", Toast.LENGTH_LONG).show();
            return;
        }
        for (Person debtor : currentDebtors) {
            currentCreditor.addDebt(currentCreditor.getName(), value, debtor.getName());
            debtor.setBalance(debtor.getBalance()-value);
            currentCreditor.setBalance(currentCreditor.getBalance()+value);
            personViewModel.update(debtor);
            mCheckboxesRecyclerView.findViewById(debtor.getId()).setBackgroundColor(getResources().getColor(R.color.white));
        }
        personViewModel.update(currentCreditor);
        mRadioGroupRecyclerView.findViewById(currentCreditor.getId()).setBackgroundColor(getResources().getColor(R.color.white));
        currentCreditor = null;
        currentDebtors = new ArrayList<>();
        mDebtAmount.setText("0");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditPersonActivity.EXTRA_NAME);
            for (Person person:peopleArraylist){
                if (person.getName().equals(name)){
                    Toast.makeText(this,"names can't repeat", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Person person = new Person(name);
            personViewModel.insert(person);
            Log.d(TAG, "onActivityResult: personInserted");
        } else {
            Log.d(TAG, "onActivityResult: personNOTinserted");
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


}
