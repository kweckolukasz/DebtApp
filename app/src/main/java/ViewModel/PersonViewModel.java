package ViewModel;

import android.app.Application;
import android.util.Log;

import java.util.List;

import Repositories.PersonRepository;
import Room.GroupWithPeople;
import Room.Person;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PersonViewModel extends AndroidViewModel {

    private PersonRepository repository;
    private LiveData<List<Person>> allPersons;

    private String TAG = PersonViewModel.class.getSimpleName();
    
    public PersonViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "PersonViewModel: inConstructor");
        repository = new PersonRepository(application);
        allPersons = repository.getAllPersons();
    }

    public void insert(Person person) {
        Log.d(TAG, "insert: "+person.getName());
        repository.insert(person);

    }

    public void update(Person person) {
        Log.d(TAG, "update: "+person.getName());
        repository.update(person);

    }
    public void delete(Person person) {
        Log.d(TAG, "delete: "+person.getName());
        repository.delete(person);

    }
    public void deleteAll() {
        Log.d(TAG, "deleteAll");
        repository.deleteAll();
    }

    public LiveData<List<Person>> getAllPersons() {
        Log.d(TAG, "getAllPersons");
        return allPersons;
    }
}
