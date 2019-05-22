package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import Adapters.PeopleListAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleListActivity extends AppCompatActivity implements PeopleListAdapter.OnPersonEditListener {

    public static final String IS_CURRENT_CREDITOR = "IS_CURRENT_CREDITOR";
    private PersonViewModel personViewModel;
    private static final String TAG = "PeopleListActivity";
    public static final int EDIT_PERSON_REQUEST = 2;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PERSON_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPersonActivity.EXTRA_ID, -1);
            boolean returnCurrentCreditor = data.getBooleanExtra(AddEditPersonActivity.IS_CURRENT_CREDITOR_EDIT, false);
            if (id == -1) {
                Log.d(TAG, "onActivityResult: person can't be edited, wrong id");
                return;
            }
            String name = data.getStringExtra(AddEditPersonActivity.EXTRA_NAME);
            Person person = new Person(name);
            if (returnCurrentCreditor) person.setCurrentCreditor(true);
            person.setId(id);
            personViewModel.update(person);
            Log.d(TAG, "onActivityResult: personEdited");
        } else {
            Log.d(TAG, "onActivityResult: personNOTedited");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_list_recycler_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("people list");
        RecyclerView mPeopleListRecyclerView = findViewById(R.id.people_list_recycler_view);
        mPeopleListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPeopleListRecyclerView.setHasFixedSize(true);
        final PeopleListAdapter peopleListAdapter = new PeopleListAdapter(this);
        mPeopleListRecyclerView.setAdapter(peopleListAdapter);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                Log.d(TAG, "onChanged");
                peopleListAdapter.setPeople(people);

            }
        });



    }


    @Override
    public void onPersonEditClick(Person person) {
        Intent intent = new Intent(PeopleListActivity.this, AddEditPersonActivity.class);
        intent.putExtra(AddEditPersonActivity.EXTRA_ID, person.getId());
        intent.putExtra(AddEditPersonActivity.EXTRA_NAME, person.getName());
        if (person.isCurrentCreditor()){
            intent.putExtra(IS_CURRENT_CREDITOR, true);
        }
        startActivityForResult(intent, EDIT_PERSON_REQUEST);
    }

    @Override
    public void onDeletePersonButton(Person person) {
        personViewModel.delete(person);
    }
}
