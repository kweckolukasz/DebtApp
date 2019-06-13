package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
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

public class DeactivatedHistoryActivity extends AppCompatActivity implements DebtAdapter.OnDebtItemListener {

    private PersonViewModel personViewModel;
    private List<Person> peopleArrayList = new ArrayList<>();
    private static final String TAG = DeactivatedHistoryActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivated_history_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("deactivated history");
        actionBar.setDisplayHomeAsUpEnabled(true);
        final RecyclerView recyclerView = findViewById(R.id.deactivated_history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
                                if (!debtSet.isActive()) debtSets.add(debtSet);
                            }
                        }
                    }
                }
                Log.d(TAG, "onChanged: sending debts to adapter: size: " + debtSets.size());

                adapter.setDebtSets(debtSets);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.deactivated_history_activity_menu, menu);
        return true;
    }

    @Override
    public void onDeleteDebtClicked(DebtSet debtSet) {
        Log.d(TAG, "onDeleteDebtClicked debtSet: "+debtSet.toString()+" date: "+debtSet.getDate());
        for (Person person: peopleArrayList){
            if (!person.getDebtSets().isEmpty()){
                Iterator iterator = person.getDebtSets().iterator();
                while(iterator.hasNext()){
                    if (iterator.next()==debtSet){
                        iterator.remove();
                        updatePerson(person);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_home:
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePerson(Person person) {
        personViewModel.update(person);
    }
}
