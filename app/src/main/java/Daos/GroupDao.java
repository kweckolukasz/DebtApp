package Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Room.Group;
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

    @Query("DELETE FROM groups")
    void deleteAll();

    @Query("SELECT * FROM groups WHERE groupId = :i")
    GroupWithPeople getGroup(int i);

    @Query("SELECT * FROM groups")
    LiveData<List<GroupWithPeople>> getGroups();

}
