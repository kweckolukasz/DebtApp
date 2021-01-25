package Room;

import android.util.Log;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "person_table")
public class Person implements Serializable, Comparable<Person> {

    @Ignore
    public String TAG = Person.class.getSimpleName();

    public Person(String name) {
        Log.d(TAG, "Person: private constructor");
        this.name = name;
        balanced = false;
        groupId = 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj.getClass().isInstance(Person.class)){
            Person person = (Person) obj;
            if (this.getId() == person.getId()) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Person o) {
        return o.getBalance() - this.balance;
    }


    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    private int personId;

    private int groupId;

    private String name;

    private String surName;

    private Integer balance = 0;

    private Integer tempBalance = 0;

    private boolean currentCreditor = false;

    private boolean currentDebtor = false;

    private boolean balanced;

    private int currentValue;

    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    /*GETTERS AND SETTERS*/
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return personId;
    }

    public void setId(int id) {
        this.personId = id;
    }

    public boolean isCurrentCreditor() {
        return currentCreditor;
    }

    public void setCurrentCreditor(boolean currentCreditor) {
        this.currentCreditor = currentCreditor;
    }

    public boolean isCurrentDebtor() {
        return currentDebtor;
    }

    public void setCurrentDebtor(boolean currentDebtor) {
        this.currentDebtor = currentDebtor;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getTempBalance() {
        return tempBalance;
    }

    public void setTempBalance(Integer tempBalance) {
        this.tempBalance = tempBalance;
    }
}
