package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import Repositories.GroupRepository;
import Room.Group;
import Room.GroupWithDebtSets;

public class HistoryViewModel extends AndroidViewModel {

    private GroupRepository groupRepository;
    private LiveData<GroupWithDebtSets> groupWithDebtSets;
    private LiveData<List<Group>> groups;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        groupRepository = new GroupRepository(application);
        groupWithDebtSets = groupRepository.getActiveGroupWithDebtSets();
        groups = groupRepository.getAllGroups();
    }

    public LiveData<GroupWithDebtSets> getGroupWithDebtSets() {
        return groupWithDebtSets;
    }

    public LiveData<List<Group>> getGroups() {
        return groups;
    }


}
