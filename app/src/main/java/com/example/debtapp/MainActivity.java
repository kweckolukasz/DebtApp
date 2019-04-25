package com.example.debtapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.debtapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Room.Person;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import supportClasses.FindingUtils;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;
    EditText mDebtAmount;
    RadioGroup mRadioGroup;
    LinearLayout mCheckboxes;
    List<Person> personArrayList;
    private PersonViewModel personViewModel;

    private static final String DEBT_AMOUNT_LESSER_THAN_0 = "cannot enter no-positive value, click change direction button to divert the arrow";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY = MainActivity.class.getSimpleName() + "global_people_list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = getApplicationContext();

        personArrayList = new ArrayList<>();
        mRadioGroup = binding.radioLeft;
        mCheckboxes = binding.checkboxes;
        keepNoMinusValuesInsideDebtAmountText();
        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {

            }
        });

    }





    private void keepNoMinusValuesInsideDebtAmountText() {
        mDebtAmount = binding.debtAmount;
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


    public void calculate(View view) {
        amount = Integer.valueOf(binding.debtAmount.getText().toString());
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
            amount = Integer.valueOf(binding.debtAmount.getText().toString());
            totalAmount = amount + amountDiff;
            binding.debtAmount.setText(String.format(Locale.US, "%d", totalAmount));
        }
        //TODO clear fields here
    }


    public void createNewPerson(View view) {


        if (binding.newPersonName.getText() != null && !binding.newPersonName.getText().toString().equals("")) {
            String personInput = binding.newPersonName.getText().toString();
            boolean isNameNotRepeat = true;


            //TODO new code here!
            if (personArrayList != null && personArrayList.size() > 0) {
                for (Person checkPerson : personArrayList) {
                    if (checkPerson.getName().equals(personInput)) {
                        isNameNotRepeat = false;
                        Toast.makeText(context, "NAME REPEAT", Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (personArrayList != null && isNameNotRepeat) {
                Person newPerson = new Person(personInput);
                personArrayList.add(newPerson);
                RadioButton rb = new RadioButton(this);
                rb.setText(personInput);
                rb.setId(personInput.hashCode());
                mRadioGroup.addView(rb);
                CheckBox cb = new CheckBox(this);
                cb.setText(personInput);
                cb.setId(personInput.hashCode());
                mCheckboxes.addView(cb);
            }
        } else {
            Toast.makeText(context, "empty input field, cannot create new person", Toast.LENGTH_LONG).show();
            Log.d(TAG, "createNewPerson: try to input empty field to create person");
        }

    }

    public void submitDebt(View view) {

        ArrayList<String> namesOfReceivers = new ArrayList<>();

        //wyciągam imię pożyczającego
        int checkedId = mRadioGroup.getCheckedRadioButtonId();
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

}
