package Room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Person.class, version = 11, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {

    private static String TAG = PersonDatabase.class.getSimpleName();

    private static PersonDatabase instance;

    public abstract PersonDao personDao();

    public static synchronized PersonDatabase getInstance(Context context){
        if (instance == null) {
            Log.d(TAG, "getInstance: ");
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDatabase.class,
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

        public PopulateDbAsyncTask(PersonDatabase db) {
            Log.d(TAG, "PopulateDbAsyncTask");
            personDao = db.personDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: inserting people");
            personDao.insert(new Person("Alice"));
            personDao.insert(new Person("Bob"));
            personDao.insert(new Person("Carol"));
            personDao.insert(new Person("Dick"));
            return null;
        }
    }
}
