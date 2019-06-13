package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapters.DebtAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class HistoryActivity extends AppCompatActivity implements DebtAdapter.OnDebtItemListener {


    private PersonViewModel personViewModel;
    private List<Person> peopleArrayList = new ArrayList<>();
    private static final String TAG = HistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("historia");
        setContentView(R.layout.activity_history);

        final RecyclerView recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final DebtAdapter adapter = new DebtAdapter(this);
        recyclerView.setAdapter(adapter);


        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {

            @Override
            public void onChanged(List<Person> people) {
                Log.d(TAG, "retrieveListOfDebts from people list - size: " + people.size());
                peopleArrayList = people;
                ArrayList<DebtSet> debtSets = new ArrayList<>();
                for (Person pe : people) {
                    if (!pe.getDebtSets().isEmpty()) {
                        for (DebtSet debtSet : pe.getDebtSets()) {
                            if (!debtSet.getDebtor().equals(debtSet.getCreditor())) {
                                if (debtSet.isActive()) debtSets.add(debtSet);
                            }
                        }
                    }
                }
                Log.d(TAG, "onChanged: sending debts to adapter: size: " + debtSets.size());
                Collections.sort(debtSets);
                adapter.setDebtSets(debtSets);
            }
        });

    }

    @Override
    public void onDeleteDebtClicked(DebtSet debtSetClicked) {
        Log.d(TAG, "onDeleteDebtClicked debtSetClicked: " + debtSetClicked.toString() + " date: " + debtSetClicked.getDate());
        for (Person creditor : peopleArrayList) {
            for (DebtSet debtSetFromArray : creditor.getDebtSets()) {
                if (debtSetFromArray == debtSetClicked) {
                    Log.d(TAG, "onDeleteDebtClicked: debtSetFromArray = debtSetClicked");
                    String debtorName = debtSetFromArray.getDebtor();
                    Person debtor = null;
                    for (Person debtorPe : peopleArrayList) {
                        if (debtorPe.getName().equals(debtorName)) {
                            debtor = debtorPe;
                        }
                    }
                    if (debtor==null) {
                        Toast.makeText(this, "debtor=null!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Integer creditorBalance = creditor.getBalance();
                    Integer debtorBalance = debtor.getBalance();
                    creditor.setBalance(creditorBalance - debtSetFromArray.getValue());
                    debtor.setBalance(debtorBalance + debtSetFromArray.getValue());
                    debtSetFromArray.setActive(false);
                    personViewModel.update(creditor);
                    personViewModel.update(debtor);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.history_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_deleted_debts:
                Intent intent = new Intent(getApplicationContext(), DeactivatedHistoryActivity.class);
                startActivity(intent);
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }
}
