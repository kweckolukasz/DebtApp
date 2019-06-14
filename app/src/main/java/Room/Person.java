package Room;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import supportClasses.DebtSet;

//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//import androidx.room.TypeConverters;

@Entity(tableName = "person_table")
public class Person implements Serializable, Comparable<Person> {

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj.getClass().isInstance(Person.class)){
            Person person = (Person) obj;
            if (this.getId() == person.getId()) return true;
        }
        return false;
    }

    @Ignore
    public String TAG = Person.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private int id;

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

    @TypeConverters(supportClasses.TypeConverters.class)
    private ArrayList<DebtSet> debtSets;

    @TypeConverters(supportClasses.TypeConverters.class)
    private ArrayList<DebtSet> moneyFlow;
    public Person(String name) {
        Log.d(TAG, "Person: private constructor");
        this.name = name;
        debtSets = new ArrayList<>();
        moneyFlow = new ArrayList<>();
        balanced = false;
    }
    public void addMoneyFlow(String creditor, Integer value, String debtor){
        moneyFlow.add(new DebtSet(creditor, value, debtor));
    }
    public void addDebt(String creditor, Integer value, String debtor){
        debtSets.add(new DebtSet(creditor,value, debtor));
    }
    public void addDebt(DebtSet debtSet){
        debtSets.add(debtSet);
    }
    @Override
    public int compareTo(Person o) {
        return o.getBalance() - this.balance;
    }

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

    public ArrayList<DebtSet> getDebtSets() {
        return debtSets;
    }

    public void setDebtSets(ArrayList<DebtSet> debtSets) {
        this.debtSets = debtSets;
    }

    public ArrayList<DebtSet> getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(ArrayList<DebtSet> moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
