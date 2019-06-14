package com.example.debtapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Adapters.PeopleListAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.Calculations;

public class PeopleListActivity extends AppCompatActivity implements PeopleListAdapter.OnPersonEditListener {

    public static final String IS_CURRENT_CREDITOR = "IS_CURRENT_CREDITOR";
    private PersonViewModel personViewModel;
    private static final String TAG = "PeopleListActivity";
    public static final int EDIT_PERSON_REQUEST = 2;
    private DeletePersonDialog deletePersonDialog = new DeletePersonDialog();
    private ArrayList<Person> peopleArrayList = new ArrayList<>();
    Calculations calculations = new Calculations();

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
                peopleArrayList = (ArrayList<Person>) people;
                peopleListAdapter.setPeople(people);
                calculations.setPeople(people);

            }
        });


    }


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
    public void onPersonEditClick(Person person) {
        Intent intent = new Intent(PeopleListActivity.this, AddEditPersonActivity.class);
        intent.putExtra(AddEditPersonActivity.EXTRA_ID, person.getId());
        intent.putExtra(AddEditPersonActivity.EXTRA_NAME, person.getName());
        if (person.isCurrentCreditor()) {
            intent.putExtra(IS_CURRENT_CREDITOR, true);
        }
        startActivityForResult(intent, EDIT_PERSON_REQUEST);
    }

    @Override
    public void onDeletePersonButton(Person person) {
        deletePersonDialog.setPerson(person);
        deletePersonDialog.setPersonViewModel(personViewModel);
        deletePersonDialog.setPeople(peopleArrayList);
        deletePersonDialog.setCalculations(calculations);
        deletePersonDialog.show(getSupportFragmentManager(), "deactivatePerson");
    }

    @Override
    public void onDeactivatePersonButton(Person person) {

        String name = person.getName();

        if (person.isActive()){
            calculations.deactivatePerson(name);
            person.setActive(false);
            for (Person person1 : peopleArrayList){
                personViewModel.update(person1);
            }
        } else {
            calculations.activatePerson(name);
            person.setActive(true);
            personViewModel.update(person);
        }

    }

    public static class DeletePersonDialog extends DialogFragment {

        private Person person;
        private PersonViewModel personViewModel;
        private ArrayList<Person> people;
        private Calculations calculations;

        public void setPeople(ArrayList<Person> people) {
            this.people = people;
        }


        public void setPersonViewModel(PersonViewModel personViewModel) {
            this.personViewModel = personViewModel;
        }

        public DeletePersonDialog() {
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public void setCalculations(Calculations calculations) {
            this.calculations = calculations;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setMessage("czy na pewno chcesz usunąć tą osobę i wraz z nią wszystkie długi w których występuje?")
                    .setPositiveButton("tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePerson();

                        }
                    })
                    .setNegativeButton("nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }

        private void deletePerson() {
            String name = person.getName();
            calculations.deactivatePerson(name);
            for (Person person1 :people){
                personViewModel.update(person1);
            }
            person.setActive(false);
        }
    }
}
