package Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;

import Daos.DebtSetDao;
import Room.AppDatabase;
import Room.DebtSet;

public class DebtSetRepository {
    private DebtSetDao debtSetDao;
    private LiveData<List<DebtSet>> debtSets;
    public DebtSetRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        debtSetDao = database.debtSetDao();
        debtSets = debtSetDao.getAllDebtSets();
    }

    public void insert(DebtSet debtSet){
        new InsertDebtSetAsyncTask(debtSetDao).execute(debtSet);
    }
    public void update(DebtSet debtSet){
        new UpdateDebtSetAsyncTask(debtSetDao).execute(debtSet);
    }
    public void delete(DebtSet debtSet){
        new DeleteDebtSetAsyncTask(debtSetDao).execute(debtSet);
    }

    public LiveData<List<DebtSet>> getAllDebtsets(){
        return debtSets;
    }

    private static class InsertDebtSetAsyncTask extends AsyncTask<DebtSet, Void, Void> {

        private DebtSetDao debtSetDAO;

        private InsertDebtSetAsyncTask(DebtSetDao debtSetDao){
            this.debtSetDAO = debtSetDao;
        }

        @Override
        protected Void doInBackground(DebtSet... debtSets) {
            debtSetDAO.insert(debtSets[0]);
            return null;
        }
    }

    private static class UpdateDebtSetAsyncTask extends AsyncTask<DebtSet, Void, Void> {

        private DebtSetDao debtSetDAO;

        private UpdateDebtSetAsyncTask(DebtSetDao debtSetDao){
            this.debtSetDAO = debtSetDao;
        }

        @Override
        protected Void doInBackground(DebtSet... debtSets) {
            debtSetDAO.update(debtSets[0]);
            return null;
        }
    }

    private static class DeleteDebtSetAsyncTask extends AsyncTask<DebtSet, Void, Void> {

        private DebtSetDao debtSetDAO;

        private DeleteDebtSetAsyncTask(DebtSetDao debtSetDao){
            this.debtSetDAO = debtSetDao;
        }

        @Override
        protected Void doInBackground(DebtSet... debtSets) {
            debtSetDAO.delete(debtSets[0]);
            return null;
        }
    }
}
