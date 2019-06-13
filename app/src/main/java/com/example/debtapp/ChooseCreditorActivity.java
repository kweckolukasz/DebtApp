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
import Room.Person;
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
    PersonViewModel personViewModel;
    public static final String CURRENT_CREDITOR_NAME = null;
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

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                Log.d(TAG, "setCreditor onChanged: ");
                setPeopleArrayList(people);
                Collections.sort(peopleArrayList, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                radioAdapter.setPeople(people);
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
            data.putExtra(CURRENT_CREDITOR_NAME, currentCreditor.getName());
            Log.d(TAG, "setCreditor saveAndExit: currentCreditor: " + currentCreditor.getName());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public void setPeopleArrayList(List<Person> personArrayList) {
        this.peopleArrayList = personArrayList;
    }
}
