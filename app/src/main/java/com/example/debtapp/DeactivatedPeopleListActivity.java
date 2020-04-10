package com.example.debtapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

import Adapters.DeactivatedPeopleListAdapter;
import Room.Person;
import ViewModel.PersonViewModel;
import supportClasses.Calculations;

public class DeactivatedPeopleListActivity extends AppCompatActivity implements DeactivatedPeopleListAdapter.OnPersonEditListener {

    private static final String TAG = DeactivatedPeopleListActivity.class.getSimpleName();
    private PersonViewModel personViewModel;
    private ArrayList<Person> peopleArraylist = new ArrayList<>();
    private ArrayList<Person> deactivatedPeopleArraylist = new ArrayList<>();
    private ArrayList<Person> activePeopleArraylist = new ArrayList<>();
    private DeletePersonDialog deletePersonDialog = new DeletePersonDialog();
private Calculations calculations = new Calculations();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivated_people_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("osoby nieaktywne");

        RecyclerView mPeopleListRecyclerView = findViewById(R.id.deactivated_people_list_recycler_view);
        mPeopleListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPeopleListRecyclerView.setHasFixedSize(true);
        final DeactivatedPeopleListAdapter peopleListAdapter = new DeactivatedPeopleListAdapter(this);
        mPeopleListRecyclerView.setAdapter(peopleListAdapter);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personViewModel.getAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                peopleArraylist = (ArrayList<Person>) people;
                for (Person person : peopleArraylist){
                    if (!person.isActive()) {
                        deactivatedPeopleArraylist.add(person);
                    } else {
                        activePeopleArraylist.add(person);
                    }

                }
                peopleListAdapter.setPeople(deactivatedPeopleArraylist);

            }
        });
    }

    @Override
    public void onPersonEditClick(Person person) {
        Intent intent = new Intent(DeactivatedPeopleListActivity.this, AddEditPersonActivity.class);
        intent.putExtra(AddEditPersonActivity.EXTRA_ID, person.getId());
        intent.putExtra(AddEditPersonActivity.EXTRA_NAME, person.getName());
        if (person.isCurrentCreditor()) {
            intent.putExtra(PeopleListActivity.IS_CURRENT_CREDITOR, true);
        }
        startActivityForResult(intent, PeopleListActivity.EDIT_PERSON_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PeopleListActivity.EDIT_PERSON_REQUEST && resultCode == RESULT_OK) {
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
    public void onDeletePersonButton(Person person) {
        deletePersonDialog.setPerson(person);
        deletePersonDialog.setPersonViewModel(personViewModel);
        deletePersonDialog.show(getSupportFragmentManager(), "deactivatePerson");
    }

    @Override
    public void onDeactivatePersonButton(Person person) {
        calculations.setPeople(activePeopleArraylist);

        calculations.activatePerson(person.getName());
        person.setActive(true);

        for (Person person1 : peopleArraylist){
            personViewModel.update(person1);
        }

        finish();
    }

    public static class DeletePersonDialog extends DialogFragment {

        private Person person;
        private PersonViewModel personViewModel;
        private ArrayList<Person> people;


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


        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder
                    .setMessage("czy na pewno chcesz usunąć tą osobę i wraz z nią wszystkie długi w których występuje?")
                    .setPositiveButton("tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            personViewModel.delete(person);
                        }
                    })
                    .setNegativeButton("nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }


    }
}
