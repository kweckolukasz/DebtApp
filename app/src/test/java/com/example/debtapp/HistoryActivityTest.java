package com.example.debtapp;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import Room.Person;


public class HistoryActivityTest {

    ArrayList<Person> people;
    HistoryActivity historyActivity;

    @BeforeAll
    void initAll(){
        people = new ArrayList<>();
        historyActivity = new HistoryActivity();

        Person alice = new Person("Alice");
        Person bob = new Person("Bob");
        Person carol = new Person("Carol");
        Person dick = new Person("Dick");

        bob.addDebt(alice.getName(),10);
        alice.addDebt(bob.getName(),-10);

        carol.addDebt(alice.getName(),10);
        alice.addDebt(carol.getName(),-10);

        carol.addDebt(bob.getName(),10);
        bob.addDebt(carol.getName(),-10);


        people.add(alice);
        people.add(bob);
        people.add(carol);
        people.add(dick);

        historyActivity.setPeople(people);
    }

    @Test
    @DisplayName("given people ArrayList ")
    public void checkIfCalcOfBalancesIsGood(){
        historyActivity.showSimplifiedSettlingAcounts();
        historyActivity.createMoneyFlows(0,people.size()-1);
        assertEquals(20,people.get(1).getMoneyFlow().get(0).getValue());

    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


}