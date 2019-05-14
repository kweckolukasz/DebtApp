package Room;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

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

    @Ignore
    public String TAG = Person.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String surName;

    private Integer balance = 0;

    private boolean CurrentCreditor = false;

    private boolean balanced;

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
        return CurrentCreditor;
    }

    public void setCurrentCreditor(boolean currentCreditor) {
        CurrentCreditor = currentCreditor;
    }
}
