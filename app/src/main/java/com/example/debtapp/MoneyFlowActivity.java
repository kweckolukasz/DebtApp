package com.example.debtapp;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import Adapters.DebtResolveAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class MoneyFlowActivity extends AppCompatActivity {

    private PersonViewModel personViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_flow_recycer_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("money flow");
        actionBar.setDisplayHomeAsUpEnabled(true);
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
