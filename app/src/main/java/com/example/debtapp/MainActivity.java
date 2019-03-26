package com.example.debtapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.debtapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Integer amount = 0;
    Integer amountDiff;
    Integer totalAmount;

    EditText debt_amount;

    private static final String DEBT_AMOUNT_LESSER_THAN_0 = "cannot enter no-positive value, click change direction button to divert the arrow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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


    public void calculutate(View view) {
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
}
