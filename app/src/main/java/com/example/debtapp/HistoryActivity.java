package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Iterator;
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

public class HistoryActivity extends AppCompatActivity implements DebtAdapter.OnDebtItemListener {


    private PersonViewModel personViewModel;
    private List<Person> peopleArrayList = new ArrayList<>();
    private ImageButton showDeletedImageButton;
    private boolean showActiveDebtSets = true;
    private static final String TAG = HistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final RecyclerView recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        showDeletedImageButton = findViewById(R.id.show_deleted_debts_image_button);
        showDeletedImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_delete_24px));
        final DebtAdapter adapter = new DebtAdapter(this);
        recyclerView.setAdapter(adapter);
        showDeletedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeactivatedHistoryActivity.class);
                startActivity(intent);
            }
        });




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

                adapter.setDebtSets(debtSets);
            }
        });

    }

    @Override
    public void onDeleteDebtClicked(DebtSet debtSet) {
        Log.d(TAG, "onDeleteDebtClicked debtSet: "+debtSet.toString()+" date: "+debtSet.getDate());
            Iterator<Person> iterator = peopleArrayList.iterator();
            for (Person person: peopleArrayList){
                if (!person.getDebtSets().isEmpty()){
                    for(DebtSet debtSet1:person.getDebtSets()){
                        if (debtSet1 == debtSet){
                            Log.d(TAG, "onDeleteDebtClicked: debtFromIterator: "+debtSet1+" from interface: "+debtSet.toString());
                            debtSet1.setActive(false);
                            personViewModel.update(iterator.next());
                        }
                    }
                }
            }


    }
}
