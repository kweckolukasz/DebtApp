package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import Room.Person;
import ViewModel.PeopleListAdapter;
import ViewModel.PersonViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleList extends AppCompatActivity implements PeopleListAdapter.OnPersonEditListener {

    private PersonViewModel personViewModel;
    private static final String TAG = "PeopleList";
    public static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPerson.EXTRA_ID, -1);
            if (id == -1){
                Log.d(TAG, "onActivityResult: person can't be edited, wrong id");
                return;
            }
            String name = data.getStringExtra(AddEditPerson.EXTRA_NAME);

            Person person = new Person(name);
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
        setContentView(R.layout.activity_people_list);
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

        peopleListAdapter.setOnPersonEditListener(new PeopleListAdapter.OnPersonEditListener() {
            @Override
            public void onPersonEditClick(Person person) {
                Intent intent = new Intent(PeopleList.this, AddEditPerson.class);
                intent.putExtra(AddEditPerson.EXTRA_ID, person.getId());
                intent.putExtra(AddEditPerson.EXTRA_NAME, person.getName());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);

            }
        });
    }


    @Override
    public void onPersonEditClick(Person person) {

    }
}
