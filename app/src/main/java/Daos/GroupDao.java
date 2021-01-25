package Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Room.Group;
import Room.GroupWithDebtSets;
import Room.GroupWithPeople;
import Room.Person;

@Dao
public interface GroupDao {

    @Insert
    void insert(Group group);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group, List<Person> people);

    @Delete
    void delete(Group group);

    @Query("DELETE FROM groups")
    void deleteAll();

    @Query("SELECT * FROM groups WHERE isActive = 'true'")
    LiveData<GroupWithPeople> getActiveGroupWithPeople();

    @Query("SELECT * FROM groups WHERE isActive = 'true'")
    LiveData<GroupWithDebtSets> getActiveGroupWithDebtSets();

    @Query("SELECT * FROM groups")
    LiveData<List<Group>> getGroups();

    @Query("SELECT * FROM groups")
    LiveData<List<GroupWithDebtSets>> getGroupsWithDebtSets();

    @Query("SELECT * FROM groups")
    LiveData<List<GroupWithPeople>> getGroupWithPeople();
}
