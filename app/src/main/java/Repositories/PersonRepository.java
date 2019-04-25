package Repositories;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import Room.Person;
import Room.PersonDao;
import Room.PersonDatabase;
import androidx.lifecycle.LiveData;

public class PersonRepository {

    private PersonDao personDao;

    private LiveData<List<Person>> allPersons;

    public PersonRepository(Application application){
        PersonDatabase database = PersonDatabase.getInstance(application);
        personDao = database.personDao();
        allPersons = personDao.getAllPersons();

    }

    public void insert(Person person){
        new InsertPersonAsyncTask(personDao).execute(person);
    }
    public void update(Person person){
        new UpdatePersonAsyncTask(personDao).execute(person);
    }
    public void delete(Person person){
        new DeletePersonAsyncTask(personDao).execute(person);
    }
    public void deleteAll(){
        new DeleteAllPersonAsyncTask(personDao).execute();
    }
    public LiveData<List<Person>> getAllPersons(){
        return allPersons;
    }


    private static class InsertPersonAsyncTask extends AsyncTask<Person, Void, Void>{

        private  PersonDao personDao;

        private InsertPersonAsyncTask(PersonDao personDao){
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.insert(people[0]);
            return null;
        }
    }

    private static class UpdatePersonAsyncTask extends AsyncTask<Person, Void, Void>{

        private  PersonDao personDao;

        private UpdatePersonAsyncTask(PersonDao personDao){
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.update(people[0]);
            return null;
        }
    }

    private static class DeletePersonAsyncTask extends AsyncTask<Person, Void, Void>{

        private  PersonDao personDao;

        private DeletePersonAsyncTask(PersonDao personDao){
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.delete(people[0]);
            return null;
        }
    }

    private static class DeleteAllPersonAsyncTask extends AsyncTask<Void, Void, Void>{

        private  PersonDao personDao;

        private DeleteAllPersonAsyncTask(PersonDao personDao){
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            personDao.deleteAllPersons();
            return null;
        }
    }



}
