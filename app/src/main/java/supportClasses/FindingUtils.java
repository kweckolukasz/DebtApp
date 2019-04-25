package supportClasses;

import java.io.Serializable;
import java.util.List;

import Room.Person;

public class FindingUtils implements Serializable {

    public static Person findPersonByName(List<Person> persons, String name) {

        for (Person p : persons) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }
}
