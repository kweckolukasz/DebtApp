package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import Repositories.DebtSetRepository;
import Repositories.GroupRepository;
import Repositories.PersonRepository;
import Room.DebtSet;
import Room.Group;
import Room.GroupWithDebtSets;
import Room.GroupWithPeople;
import Room.Person;

public class MainActivityViewModel extends AndroidViewModel {

    private GroupRepository repo;
    private PersonRepository personRepo;
    private DebtSetRepository debtSetRepo;
    private LiveData<List<Group>> groups;
    private LiveData<List<GroupWithPeople>> groupsWithPeople;
    private LiveData<List<GroupWithDebtSets>> groupsWithDebtSets;
    private LiveData<GroupWithPeople> activeGroupWithPeople;
    private LiveData<GroupWithDebtSets> activeGroupWithDebtSets;



    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new GroupRepository(application);
        personRepo = new PersonRepository(application);
        debtSetRepo = new DebtSetRepository(application);
        groups = repo.getAllGroups();
        groupsWithDebtSets = repo.getAllGroupsWithDebtSets();
        groupsWithPeople = repo.getAllGroupsWithPeople();
        activeGroupWithPeople = repo.getActiveGroupWithPeople();
        activeGroupWithDebtSets = repo.getActiveGroupWithDebtSets();
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
    public void insert(Person person) {
        personRepo.insert(person);
    }
    public void update(Person person) {
        personRepo.update(person);
    }
    public void insert(DebtSet debtSet) {
        debtSetRepo.insert(debtSet);
    }

    public LiveData<GroupWithPeople> getActiveGroupWithPeople(){
        return activeGroupWithPeople;
    }
    public LiveData<GroupWithDebtSets> getActiveGroupWithDebtSets() {
        return activeGroupWithDebtSets;
    }
    public LiveData<List<Group>> getAllGroups(){
        return groups;
    }
    public LiveData<List<GroupWithPeople>> getGroupsWithPeople() {
        return groupsWithPeople;
    }
    public LiveData<List<GroupWithDebtSets>> getGroupsWithDebtSets() {
        return groupsWithDebtSets;
    }

    public Group getActiveGroup(){
        if (groups.getValue() != null){
            for (Group group :
                    groups.getValue()) {
                if (group.isActive()) return group;
            }
        } else throw new NullPointerException("There are no List<Group> inside LiveData<>");
        return null;
    }
}
