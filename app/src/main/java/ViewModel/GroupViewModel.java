package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import Repositories.GroupRepository;
import Room.Group;
import Room.GroupWithDebtSets;
import Room.GroupWithPeople;

public class GroupViewModel extends AndroidViewModel {

    private GroupRepository repo;
    private LiveData<List<Group>> groups;
    private LiveData<List<GroupWithPeople>> groupsWithPeople;
    private LiveData<List<GroupWithDebtSets>> groupsWithDebtSets;
    private LiveData<GroupWithPeople> activeGroupWithPeople;



    public GroupViewModel(@NonNull Application application) {
        super(application);
        repo = new GroupRepository(application);
        groups = repo.getAllGroups();
        groupsWithDebtSets = repo.getAllGroupsWithDebtSets();
        groupsWithPeople = repo.getAllGroupsWithPeople();
        activeGroupWithPeople = repo.getActiveGroupWithPeople();
    }

    public void insert(Group group){
        repo.insert(group);
    }
    public void update(Group group){
        repo.update(group);
    }
    public void delete(Group group){
        repo.delete(group);
    }
    public LiveData<GroupWithPeople> getActiveGroupWithPeople(){return activeGroupWithPeople;}
    public LiveData<List<Group>> getAllGroups(){
        return groups;
    }
    public LiveData<List<GroupWithPeople>> getGroupsWithPeople() {
        return groupsWithPeople;
    }
    public LiveData<List<GroupWithDebtSets>> getGroupsWithDebtSets() {
        return groupsWithDebtSets;
    }
}
