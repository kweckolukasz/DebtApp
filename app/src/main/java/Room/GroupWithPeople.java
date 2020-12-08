package Room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GroupWithPeople {
    @Embedded public Person person;
    @Relation(
            parentColumn = "groupId",
            entityColumn = "personInGroupId"
    )
    public List<Person> peopleInGroup;

}
