package com.example.debtapp;


import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import Room.Person;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<HistoryActivity> activityTestRule = new ActivityTestRule<>(HistoryActivity.class);


    @Before
    public void createAndStartIntent(){
        ArrayList<Person> personArrayList = new ArrayList<>();
        Person p1 = new Person("Alice");
        Person p2 = new Person("Bob");
        Person p3 = new Person("Carol");
        Person p4 = new Person("Dick");
        personArrayList.add(p1);
        personArrayList.add(p2);
        personArrayList.add(p3);
        personArrayList.add(p4);
        Intent intent = new Intent();
        intent.putExtra(MainActivity.DEBT_MAIN_PEOPLE_LIST_MAIN_ACTIVITY, personArrayList);
        activityTestRule.launchActivity(intent);
    }

    @Test
    public void useAppContext() {

    }


}
