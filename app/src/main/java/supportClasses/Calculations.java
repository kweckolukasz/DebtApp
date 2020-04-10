package supportClasses;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Room.Person;

public class Calculations {

    private static final String TAG = Calculations.class.getSimpleName();
    private List<Person> people = new ArrayList<>();

    public void setPeople(List<Person> people) {
        Log.d(TAG, "setPeople:");
        this.people = people;
    }

    public void deactivatePerson(String name) {

        for (Person person1 : people) {

            if (person1.isActive()) {

                if (person1.getDebtSets() != null || person1.getDebtSets().size() != 0) {

                    ArrayList<DebtSet> debtSets = person1.getDebtSets();

                    for (DebtSet debtSet : debtSets) {

                        if (debtSet.isActive() && (debtSet.getCreditor().equals(name) || debtSet.getDebtor().equals(name))) {

                            Person creditor = person1;
                            Person debtor;
                            for (Person deb : people) {
                                if (deb.getName().equals(debtSet.getDebtor())) {
                                    debtor = deb;
                                    debtor.setBalance(debtor.getBalance() + debtSet.getValue());
                                    creditor.setBalance(creditor.getBalance() - debtSet.getValue());
                                }

                            }
                            debtSet.setActive(false);
                        }
                    }
                }
            }
        }
    }

    public void activatePerson(String name) {

        for (Person person1 : people) {

            if (person1.getDebtSets() != null || person1.getDebtSets().size() != 0) {
                ArrayList<DebtSet> debtSets = person1.getDebtSets();

                for (DebtSet debtSet : debtSets) {

                    if (!debtSet.isActive() && (debtSet.getCreditor().equals(name) || debtSet.getDebtor().equals(name))) {

                        Person creditor = person1;
                        Person debtor;
                        for (Person deb : people) {
                            if (deb.getName().equals(debtSet.getDebtor())) {
                                debtor = deb;
                                debtor.setBalance(debtor.getBalance() - debtSet.getValue());
                                creditor.setBalance(creditor.getBalance() + debtSet.getValue());
                            }

                        }
                        debtSet.setActive(true);
                    }
                }
            }
        }
    }
}
