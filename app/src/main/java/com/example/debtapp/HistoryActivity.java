package com.example.debtapp;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Adapters.DebtAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class HistoryActivity extends AppCompatActivity {


    private PersonViewModel personViewModel;

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
                Log.d(TAG, "retrieveListOfDebts from people list - size: " + people.size());
                ArrayList<DebtSet> debtSets = new ArrayList<>();
                for (Person pe : people) {
                    if (!pe.getDebtSets().isEmpty()) {
                        for (DebtSet debtSet : pe.getDebtSets()) {
                            if (!debtSet.getDebtor().equals(debtSet.getCreditor())) {
                                debtSets.add(debtSet);
                            }
                        }
                    }
                }
                Log.d(TAG, "onChanged: sending debts to adapter: size: " + debtSets.size());

                adapter.setDebtSets(debtSets);
            }
        });

    }
}
