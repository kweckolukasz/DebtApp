package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adapters.RadioAdapter;
import Room.GroupWithPeople;
import Room.Person;
import ViewModel.MainActivityViewModel;
import ViewModel.PersonViewModel;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseCreditorActivity extends AppCompatActivity implements RadioAdapter.OnPersonRadioListener {

    Person currentCreditor;
    List<Person> peopleArrayList = new ArrayList<>();

    RecyclerView mRadioGroupRecyclerView;
    MainActivityViewModel mainActivityViewModel;
    public static final String CURRENT_CREDITOR = "CurrentCreditorId";
    public static final String TAG = ChooseCreditorActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("choose creditor");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_choose_creditor);
        mRadioGroupRecyclerView = findViewById(R.id.radio_left);

        mRadioGroupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRadioGroupRecyclerView.setHasFixedSize(true);

        final RadioAdapter radioAdapter = new RadioAdapter(this);
        mRadioGroupRecyclerView.setAdapter(radioAdapter);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getActiveGroupWithPeople().observe(this, new Observer<GroupWithPeople>() {
            @Override
            public void onChanged(GroupWithPeople groupWithPeople) {
                peopleArrayList = groupWithPeople.peopleInGroup;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_new_person_menu, menu);
        setTitle("choose Creditor");
        return true;
    }

    @Override
    public void onPersonRadioClick(Person person) {
        currentCreditor = person;
        Log.d(TAG, "setCreditor onPersonRadioClick: creditor choosen: " + person.getName());
        saveAndExit();
    }

    private void saveAndExit() {
        if (currentCreditor != null) {
            Intent data = new Intent(this, MainActivity.class);
            data.putExtra(CURRENT_CREDITOR, currentCreditor.getId());
            setResult(RESULT_OK, data);
            finish();
        }
    }

}
