package ViewModel;

import android.app.Application;

import java.util.List;

import Repositories.PersonRepository;
import Room.Person;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PersonViewModel extends AndroidViewModel {

    private PersonRepository repository;
    private LiveData<List<Person>> allPersons;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        allPersons = repository.getAllPersons();
    }

    public void insert(Person person) {
        repository.insert(person);

    }

    public void update(Person person) {
        repository.update(person);

    }
    public void delete(Person person) {
        repository.delete(person);

    }
    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Person>> getAllPersons() {
        return allPersons;
    }
}
