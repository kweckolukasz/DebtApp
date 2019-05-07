package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Room.Person;
import ViewModel.CheckboxesAdapter;
import ViewModel.PersonViewModel;
import ViewModel.RadioAdapter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements CheckboxesAdapter.OnPeopleCheckboxesListener, RadioAdapter.OnPersonRadioListener {

    Context context;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;
    EditText mDebtAmount;
    RecyclerView mRadioGroupRecyclerView;
    RecyclerView mCheckboxesRecyclerView;

//    Button mAddEditPersonButton;
//    Button mListOfPeopleButton;


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
//        mAddEditPersonButton = findViewById(R.id.button_add_new_person);
        mRadioGroupRecyclerView = findViewById(R.id.radio_left);
        mCheckboxesRecyclerView = findViewById(R.id.checkboxes);
        mDebtAmount = findViewById(R.id.debt_amount);
//        mListOfPeopleButton = findViewById(R.id.button_show_list_of_people);

        keepNoMinusValuesInsideDebtAmountText();

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

//        mAddEditPersonButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddEditPerson.class);
//                startActivityForResult(intent, ADD_PERSON_REQUEST);
//            }
//        });
//        mListOfPeopleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PeopleList.class);
//                startActivity(intent);
//            }
//        });
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
//            person.setCurrentCreditor(true);
//            personViewModel.update(person);
            for (Person person1 : peopleArraylist) {
                if (!person1.equals(currentCreditor)) {
//                        person1.setCurrentCreditor(false);
//                        personViewModel.update(person1);
                    mRadioGroupRecyclerView.findViewById(person1.getId()).setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
            return;
        }
        if (person.equals(currentCreditor)){
            currentCreditor = null;
//            person.setCurrentCreditor(false);
//            personViewModel.update(person);
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
            //            person.setCurrentDebtor(true);
//            personViewModel.update(person);
            currentDebtors.add(person);
            mCheckboxesRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.green));

        } else {
//            person.setCurrentDebtor(false);
//            personViewModel.update(person);
            currentDebtors.remove(person);
            mCheckboxesRecyclerView.findViewById(id).setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditPerson.EXTRA_NAME);
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
                Intent intent = new Intent(MainActivity.this, AddEditPerson.class);
                startActivityForResult(intent, ADD_PERSON_REQUEST);
                return true;
            case R.id.show_list_of_persons_item:
                Intent intent2 = new Intent(MainActivity.this, PeopleList.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void keepNoMinusValuesInsideDebtAmountText() {
//        mDebtAmount = binding.debtAmount;
        if (mDebtAmount != null) {
            mDebtAmount.addTextChangedListener(new TextWatcher() {

                int startValue;
                int changedValue;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (s.length() > 0)
                        startValue = Integer.valueOf(mDebtAmount.getText().toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        mDebtAmount.setText("0");
                    } else {
                        changedValue = Integer.valueOf(s.toString());
                        if (changedValue < 0) {
                            Toast.makeText(getApplicationContext(), DEBT_AMOUNT_LESSER_THAN_0, Toast.LENGTH_LONG).show();
                            mDebtAmount.setText(String.format("%d", startValue));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
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
        //TODO clear fields here
    }

    public void submitDebt(View view) {
        Integer value = Integer.valueOf(mDebtAmount.getText().toString());
        if (value <= 0) {
            Toast.makeText(this, "debt can't be 0 or less", Toast.LENGTH_LONG).show();
            return;
        }
        if (currentCreditor == null || currentDebtors.size()==0){
            Toast.makeText(this, "have to choose at least one creditor and one debtor", Toast.LENGTH_LONG).show();
            return;
        }
        //ustawiam debtSety
        for (Person debtor : currentDebtors) {
            currentCreditor.addDebt(currentCreditor.getName(), value, debtor.getName());
            debtor.setBalance(debtor.getBalance()-value);
            personViewModel.update(debtor);
            mCheckboxesRecyclerView.findViewById(debtor.getId()).setBackgroundColor(getResources().getColor(R.color.white));
            currentCreditor.setBalance(currentCreditor.getBalance()+value);
        }
        personViewModel.update(currentCreditor);
        mCheckboxesRecyclerView.findViewById(currentCreditor.getId()).setBackgroundColor(getResources().getColor(R.color.white));
        currentCreditor = null;

    }

    public void showDebts(View view) {
        //TODO zapisz global map do bundle i InstateState
        Intent intent = new Intent(this, HistoryActivity.class);
        //intent.putExtra(DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY, peopleArraylist);
        Log.d(TAG, "showDebts: starting HistoryActivity");
        startActivity(intent);
    }


    public void setPeopleArraylist(List<Person> peopleArraylist) {
        this.peopleArraylist = peopleArraylist;
    }


}
