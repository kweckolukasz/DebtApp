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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Room.Person;
import ViewModel.PeopleAdapter;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.FindingUtils;

public class MainActivity extends AppCompatActivity implements PeopleAdapter.OnPersonListener {

//    ActivityMainBinding binding;
    Context context;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;
    EditText mDebtAmount;
    EditText mNewPersonName;
    RecyclerView mRadioGroup;
    RecyclerView mCheckboxes;
    FloatingActionButton mAddPerson;

    List<Person> personArrayList = new ArrayList<>();

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
        mAddPerson = findViewById(R.id.fab_add_new_person);
        mRadioGroup = findViewById(R.id.radio_left);
        mCheckboxes = findViewById(R.id.checkboxes);
        mDebtAmount = findViewById(R.id.debt_amount);
        keepNoMinusValuesInsideDebtAmountText();

        mCheckboxes.setLayoutManager(new LinearLayoutManager(this));
        mCheckboxes.setHasFixedSize(true);

        mRadioGroup.setLayoutManager(new LinearLayoutManager(this));
        mRadioGroup.setHasFixedSize(true);

        final PeopleAdapter adapter = new PeopleAdapter(this);
        mCheckboxes.setAdapter(adapter);
        mRadioGroup.setAdapter(adapter);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> people) {
                adapter.setPeople(people);
                Log.d(TAG, "personViewModel -> observer -> onChanged");
            }
        });

        mAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddPerson.class);
                startActivityForResult(intent,ADD_PERSON_REQUEST);
            }
        });
        Log.d(TAG, "onCreate: starting populate Bars");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddPerson.EXTRA_NAME);
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
        menuInflater.inflate(R.menu.delete_all_persons_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_item:
                personViewModel.deleteAll();
                return true;
            default:return super.onOptionsItemSelected(item);
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
                    if (s.length() > 0) startValue = Integer.valueOf(mDebtAmount.getText().toString());
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

        ArrayList<String> namesOfReceivers = new ArrayList<>();

        //wyciągam imię pożyczającego
        //TODO już nie masz RadioGroup!
        //int checkedId = mRadioGroup.getCheckedRadioButtonId();
        int checkedId = 123;
        RadioButton checked = mRadioGroup.findViewById(checkedId);
        String debtGiverName = checked.getText().toString();

        //wyciągam imiona wierzycieli
        for (int i = 0; i < mCheckboxes.getChildCount(); i++) {
            View potentialCheckbox = mCheckboxes.getChildAt(i);
            if (potentialCheckbox instanceof CheckBox) {
                CheckBox potentialReceiver = (CheckBox) potentialCheckbox;
                if (potentialReceiver.isChecked()) {
                    String receiverName = potentialReceiver.getText().toString();
                    namesOfReceivers.add(receiverName);
                }
            }
        }

        amount = Integer.valueOf(mDebtAmount.getText().toString());

        //TODO new code here!
        //znajduje obiekt Person szukając po wyciągniętym imieniu
        Person debtGiverNew = FindingUtils.findPersonByName(personArrayList, debtGiverName);

        if (debtGiverName != null) {
            for (String name : namesOfReceivers) {
                debtGiverNew.addDebt(name, amount);
                FindingUtils.findPersonByName(personArrayList, name).addDebt(debtGiverName, amount * (-1));
            }
        } else {
            Log.d(TAG, "submitDebt() debtGiverName not found in personArraylist");
        }
    }

    public void showDebts(View view) {
        //TODO zapisz global map do bundle i InstateState
        Intent intent = new Intent(this, HistoryActivity.class);
        //intent.putExtra(DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY, personArrayList);
        Log.d(TAG, "showDebts: starting HistoryActivity");
        startActivity(intent);

    }


    public void setPersonArrayList(List<Person> personArrayList) {
        this.personArrayList = personArrayList;
    }

    @Override
    public void onPersonClick(int position) {

    }
}
