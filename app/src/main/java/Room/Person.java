package Room;

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

    public Person(int id, String name, String surName, ArrayList<DebtSet> debtSets, ArrayList<DebtSet> moneyFlow, Integer balance, boolean balanced) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.debtSets = debtSets;
        this.moneyFlow = moneyFlow;
        this.balance = balance;
        this.balanced = balanced;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    @TypeConverters(supportClasses.TypeConverters.class)
    private ArrayList<DebtSet> debtSets;
    @TypeConverters(supportClasses.TypeConverters.class)
    private ArrayList<DebtSet> moneyFlow;
    private String surName;
    private Integer balance;
    private boolean balanced;

    @Ignore
    public Person(String name) {
        this.name = name;
        debtSets = new ArrayList<>();
        moneyFlow = new ArrayList<>();
        balanced = false;
    }


    public void addMoneyFlow(String name, Integer value){
        moneyFlow.add(new DebtSet(name, value));
    }

    public void addDebt(String name, Integer value){
        debtSets.add(new DebtSet(name,value));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public ArrayList<DebtSet> getDebtSets() {
        return debtSets;
    }

    public void setDebtSets(ArrayList<DebtSet> debtSets) {
        this.debtSets = debtSets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<DebtSet> getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(ArrayList<DebtSet> moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    @Override
    public int compareTo(Person o) {
        return o.getBalance() - this.balance;
    }
}
