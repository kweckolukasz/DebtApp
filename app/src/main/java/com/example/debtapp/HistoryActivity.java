package com.example.debtapp;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Room.Person;
import ViewModel.DebtAdapter;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class HistoryActivity extends AppCompatActivity {


    private PersonViewModel personViewModel;
//    ArrayList<Person> people;
//    LinearLayout mDebtGiverNameCol;
//    LinearLayout mAmountCol;
//    LinearLayout mReceiverNameCol;
//    LinearLayout mAccountSetling;


    private static final String TAG = HistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final DebtAdapter adapter = new DebtAdapter();
        recyclerView.setAdapter(adapter);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {

            @Override
            public void onChanged(List<Person> people) {
                    Log.d(TAG, "retrieveListOfDebts from people list - size: "+people.size());
                    ArrayList<DebtSet> debtSets = new ArrayList<>();
                    for (Person pe : people) {
                        if (!pe.getDebtSets().isEmpty()) debtSets.addAll(pe.getDebtSets());
                    }

                adapter.setDebtSets(debtSets);
            }
        });



//        readDataFromMainActivity();
//        showSimplifiedSettlingAcounts();
//        createMoneyFlows(0, people.size() - 1);
//        displayMoneyFlow();

//        mDebtGiverNameCol = binding.name;
//        mAmountCol = binding.amount;
//        mReceiverNameCol = binding.receiverName;
//        mAccountSetling = binding.accountSettling;
//        people = (ArrayList<Person>) getIntent().getSerializableExtra(MainActivity.DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY);
    }

//    private void readDataFromMainActivity() {
//        //TODO new code here!
//        for (Person person : people) {
//            if (person.getDebtSets() != null) {
//                ArrayList<DebtSet> personDebts = person.getDebtSets();
//                for (DebtSet debt : personDebts) {
//                    Integer value = (Integer) debt.getValue();
//                    if (value > 0) {
//                        TextView name = new TextView(getApplicationContext());
//                        name.setText(person.getCreditor());
//                        name.setTypeface(null, Typeface.BOLD);
//                        name.setTextColor(getResources().getColor(R.color.black));
//                        //mDebtGiverNameCol.addView(name);
//
//
//                        TextView amount = new TextView(getApplicationContext());
//                        amount.setText(String.format(Locale.US, "%d", value));
//                        amount.setTypeface(null, Typeface.BOLD);
//                        amount.setTextColor(getResources().getColor(R.color.white));
//                        amount.setBackgroundColor(getResources().getColor(R.color.green));
//                        //mAmountCol.addView(amount);
//
//                        TextView receiver = new TextView(getApplicationContext());
//                        receiver.setText((String) debt.getCreditor());
//                        receiver.setTextColor(getResources().getColor(R.color.black));
//                        receiver.setTypeface(null, Typeface.BOLD);
//                        //mReceiverNameCol.addView(receiver);
//                    }
//                }
//            } else {
//                Log.d(TAG, "readDataFromMainActivity: attempted to read null personDebts arraylist");
//            }
//        }
//    }
//
//    public void showSimplifiedSettlingAcounts() {
//
//        Integer p = 0;
//        for (Person person : people) {
//            Integer balance = 0;
//            if (person.getDebtSets() != null) {
//                ArrayList<DebtSet> personDebtSets = person.getDebtSets();
//                for (DebtSet singleDebt : personDebtSets) {
//                    Integer value = (Integer) singleDebt.getValue();
//                    balance = balance + value;
//                    p = p + value;
//                }
//            }
//            person.setBalance(balance);
//        }
//        Collections.sort(people);
//        if (p == 0) {
//            Toast.makeText(this, "sum of all balances equal: " + p, Toast.LENGTH_LONG).show();
//
//            for (Person person : people) {
//                TextView account_settling = new TextView(this);
//                account_settling.setTypeface(null, Typeface.BOLD);
//                account_settling.setTextColor(getResources().getColor(R.color.black));
//                String textViewString = person.getCreditor() + " have balance of: " + person.getBalance().toString();
//                account_settling.setText(textViewString);
//                //mAccountSetling.addView(account_settling);
//            }
//        }
//    }
//
//    public void createMoneyFlows(int creditorIndex, int debtorIndex) {
//
//        Person creditor = people.get(creditorIndex);
//        Person debtor = people.get(debtorIndex);
//        //TODO do optymalizacji: metoda powinna wyszukiwać takich połączeń w których zachodzi equals, eliminować, po tym robić poniższe
//        if (debtor.getBalance().compareTo(0)<0) {
//            if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance()))>0) {
//                debtor.addMoneyFlow(creditor.getCreditor(), debtor.getBalance());
//                creditor.setBalance(creditor.getBalance() + debtor.getBalance());
//                debtor.setBalance(0);
//                debtor.setBalanced(true);
//                debtorIndex--;
//                createMoneyFlows(creditorIndex, debtorIndex);
//            } else if (creditor.getBalance().equals(Math.abs(debtor.getBalance()))) {
//                creditor.setBalance(0);
//                creditor.setBalanced(true);
//                creditorIndex++;
//                debtor.addMoneyFlow(creditor.getCreditor(), debtor.getBalance());
//                debtor.setBalance(0);
//                debtor.setBalanced(true);
//                debtorIndex--;
//                createMoneyFlows(creditorIndex, debtorIndex);
//            } else if (creditor.getBalance().compareTo(Math.abs(debtor.getBalance()))<0) {
//                debtor.setBalance(debtor.getBalance() + creditor.getBalance());
//                debtor.addMoneyFlow(creditor.getCreditor(), creditor.getBalance());
//                creditor.setBalanced(true);
//                creditor.setBalance(0);
//                creditorIndex++;
//                createMoneyFlows(creditorIndex, debtorIndex);
//            }
//        }
//
//
//    }
//
//    public void displayMoneyFlow() {
//        TextView test = new TextView(this);
//        //mAccountSetling.addView(test);
//        for (Person person : people) {
//            TextView t = new TextView(this);
//           if (person.getMoneyFlow().size() != 0){
//               for (DebtSet debtSet : person.getMoneyFlow()) {
//                   t.setText(person.getCreditor()+" money flow size: "+person.getMoneyFlow().size());
//                   StringBuilder sb = new StringBuilder();
//                   sb.append(person.getCreditor());
//                   sb.append(" musi oddać " + debtSet.getValue());
//                   sb.append(" PLN dla: " + debtSet.getCreditor());
//                   t.setText(sb.toString());
//               }
//               //mAccountSetling.addView(t);
//           }else {
//               t.setText(person.getCreditor()+" money flow empty");
//               //mAccountSetling.addView(t);
//           }
//
//        }
//    }



//    @Override
//    protected void onDestroy() {
//        Gson gson = new Gson();
//        for (Person p :people){
//            String jsonHistory = gson.toJson(p);
//        }
//        super.onDestroy();
//    }
}
