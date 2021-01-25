package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapters.DebtAdapter;
import Room.DebtSet;
import Room.GroupWithDebtSets;
import Room.Person;
import ViewModel.HistoryViewModel;
import ViewModel.PersonViewModel;

public class HistoryActivity extends AppCompatActivity implements DebtAdapter.OnDebtItemListener {


    private HistoryViewModel historyViewModel;
    private static final String TAG = HistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("historia");

        final RecyclerView recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final DebtAdapter adapter = new DebtAdapter(this);
        recyclerView.setAdapter(adapter);


        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getGroupWithDebtSets().observe(this, new Observer<GroupWithDebtSets>() {
            @Override
            public void onChanged(GroupWithDebtSets groupWithDebtSets) {
                adapter.setDebtSets((ArrayList<DebtSet>) groupWithDebtSets.debtSetsInList);
            }
        });

    }

    @Override
    public void onDeleteDebtClicked(DebtSet debtSetClicked) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.history_activity_menu, menu);
        return true;
    }


}
