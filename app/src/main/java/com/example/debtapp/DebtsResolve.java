package com.example.debtapp;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import Room.Person;
import ViewModel.DebtResolveAdapter;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class DebtsResolve extends AppCompatActivity {

    private PersonViewModel personViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debts_resolve);
        RecyclerView mMoneyFlowRecyclerView = findViewById(R.id.resolve_debt_recyclerView);
        mMoneyFlowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMoneyFlowRecyclerView.setHasFixedSize(true);
        final DebtResolveAdapter debtResolveAdapter = new DebtResolveAdapter();
        mMoneyFlowRecyclerView.setAdapter(debtResolveAdapter);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                ArrayList<DebtSet> allMoneyFlows = new ArrayList<>();
                for (Person person:people){
                    allMoneyFlows.addAll(person.getMoneyFlow());
                }
                debtResolveAdapter.setAllMoneyFlow(allMoneyFlows);
            }
        });
    }
}
