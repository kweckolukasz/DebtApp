package com.example.debtapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.debtapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context mContext;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;
    List<String> people;
    EditText debt_amount;
    RadioGroup rg;
    LinearLayout ll;
    Map<String, HashMap> globalMap;

    private static final String DEBT_AMOUNT_LESSER_THAN_0 = "cannot enter no-positive value, click change direction button to divert the arrow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = getApplicationContext();
        people = new ArrayList<>();
        rg = binding.radioLeft;
        ll = binding.checkboxes;
        globalMap = new HashMap<>();
        keepNoMinusValuesInsideDebtAmountText();


    }


    private void keepNoMinusValuesInsideDebtAmountText() {
        debt_amount = binding.debtAmount;
        debt_amount.addTextChangedListener(new TextWatcher() {

            int startValue;
            int changedValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) startValue = Integer.valueOf(debt_amount.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    debt_amount.setText("0");
                } else {
                    changedValue = Integer.valueOf(s.toString());
                    if (changedValue < 0) {
                        Toast.makeText(getApplicationContext(), DEBT_AMOUNT_LESSER_THAN_0, Toast.LENGTH_LONG).show();
                        debt_amount.setText(String.format("%d", startValue));
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
            updateDebtAmount();
        } //else Toast.makeText(getApplicationContext(), DEBT_AMOUNT_LESSER_THAN_0, Toast.LENGTH_LONG).show();
    }

    public void updateDebtAmount() {
        amount = Integer.valueOf(binding.debtAmount.getText().toString());
        totalAmount = amount + amountDiff;
        binding.debtAmount.setText(String.format("%d", totalAmount));
    }

    public void createNewPerson(View view) {
        String personInput = binding.newPersonName.getText().toString();
        boolean isNameNotRepeat = true;
        if (people != null && people.size() > 0) {
            for (String checkPerson :
                    people) {
                if (checkPerson.equals(personInput)) {
                    isNameNotRepeat = false;
                    Toast.makeText(getApplicationContext(), "NAME REPEAT", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (isNameNotRepeat && people != null) {

            HashMap<String, Integer> individualMap = new HashMap<>();
            globalMap.put(personInput, individualMap);

            people.add(personInput);
            RadioButton rb = new RadioButton(this);
            rb.setText(personInput);
            rb.setId(personInput.hashCode());
            rg.addView(rb);
            CheckBox cb = new CheckBox(this);
            cb.setText(personInput);
            cb.setId(personInput.hashCode());
            ll.addView(cb);
        }

    }

    public void submitDebt(View view) {

        //wyciągam imię pożyczającego
        int checkedId = binding.radioLeft.getCheckedRadioButtonId();
        RadioButton checked = rg.findViewById(checkedId);
        String debtGiver = checked.getText().toString();

        //wyciągam imiona wierzycieli
        ArrayList<String> receivers = new ArrayList();
        LinearLayout checkboxes = binding.checkboxes;
        for (int i = 0; i < checkboxes.getChildCount(); i++) {
            View potentialCheckbox = checkboxes.getChildAt(i);
            if (potentialCheckbox instanceof CheckBox) {
                CheckBox potentialReceiver = (CheckBox) potentialCheckbox;
                if (potentialReceiver.isChecked()) {
                    String receiverName = potentialReceiver.getText().toString();
                    receivers.add(receiverName);
                }
            }
        }

        amount = Integer.valueOf(binding.debtAmount.getText().toString());
        HashMap debtGiverMap = globalMap.get(debtGiver);
        for (String debtReceiver :
                receivers) {
            debtGiverMap.put(debtReceiver, amount);
            globalMap.get(debtReceiver).put(debtGiver,amount*(-1));
        }


    }
}
