package Room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Daos.DebtSetDao;
import Daos.GroupDao;
import Daos.PersonDao;

@Database(entities = {Person.class, Group.class, DebtSet.class}, version = 13, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase instance;

    public abstract PersonDao personDao();
    public abstract GroupDao groupDao();
    public abstract DebtSetDao debtSetDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null) {
            Log.d(TAG, "getInstance: ");
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "person_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(TAG, "onCreate: RoomDatabase.Callback");
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PersonDao personDao;
        private GroupDao groupDao;

        public PopulateDbAsyncTask(AppDatabase db) {
            Log.d(TAG, "PopulateDbAsyncTask");
            personDao = db.personDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: inserting people");
            Group group = new Group("default", true);
            groupDao.insert(group);

            List<Person> people = new ArrayList<>(Arrays.asList(
                    new Person("Alice"){
                        @Override
                        public void setGroupId(int groupId) {
                            super.setGroupId(0);
                        }
                    },
                    new Person("Bob"){
                        @Override
                        public void setGroupId(int groupId) {
                            super.setGroupId(0);
                        }
                    },
                    new Person("Carol"){
                        @Override
                        public void setGroupId(int groupId) {
                            super.setGroupId(0);
                        }
                    },
                    new Person("Dick"){
                        @Override
                        public void setGroupId(int groupId) {
                            super.setGroupId(0);
                        }
                    }

            ));
            for (Person person :
                    people) {
                personDao.insert(person);
            }

            return null;
        }
    }
}
