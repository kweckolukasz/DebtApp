package Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import Daos.GroupDao;
import Room.AppDatabase;
import Room.Group;
import Room.GroupWithDebtSets;
import Room.GroupWithPeople;

public class GroupRepository {
    private String TAG = GroupRepository.class.getSimpleName();
    private GroupDao groupDao;
    private LiveData<List<GroupWithPeople>> groupsWithPeople;
    private LiveData<List<GroupWithDebtSets>> groupsWithDebtSets;
    private LiveData<List<Group>> groups;
    private LiveData<GroupWithPeople> activeGroupWithPeople;

    public GroupRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        groupDao = database.groupDao();
        groupsWithDebtSets = groupDao.getGroupsWithDebtSets();
        groups = groupDao.getGroups();
        groupsWithPeople = groupDao.getGroupWithPeople();
        activeGroupWithPeople = groupDao.getActiveGroupWithPeople();
    }

    public void insert(Group group){
        new InsertGroupAsyncTask(groupDao).execute(group);
    }

    public void update(Group group){
        new UpdateGroupAsyncTask(groupDao).execute(group);
    }

    public void delete(Group group){
        new DeleteGroupAsyncTask(groupDao).execute(group);
    }

    public LiveData<GroupWithPeople> getActiveGroupWithPeople(){return activeGroupWithPeople;}

    public LiveData<List<GroupWithPeople>> getAllGroupsWithPeople(){
        return groupsWithPeople;
    }

    public LiveData<List<GroupWithDebtSets>> getAllGroupsWithDebtSets(){
        return groupsWithDebtSets;
    }

    public LiveData<List<Group>> getAllGroups(){
        return groups;
    }

    private static class InsertGroupAsyncTask extends AsyncTask<Group, Void, Void>{

        private GroupDao groupDao;

        private InsertGroupAsyncTask(GroupDao groupDao){
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDao.insert(groups[0]);
            return null;
        }
    }

    private static class UpdateGroupAsyncTask extends AsyncTask<Group, Void, Void>{

        private GroupDao groupDao;

        private UpdateGroupAsyncTask(GroupDao groupDao){
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDao.update(groups[0]);
            return null;
        }
    }

    private static class DeleteGroupAsyncTask extends AsyncTask<Group, Void, Void>{

        private GroupDao groupDao;

        private DeleteGroupAsyncTask(GroupDao groupDao){
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDao.delete(groups[0]);
            return null;
        }
    }

}
