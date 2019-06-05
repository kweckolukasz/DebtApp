package supportClasses;

import java.io.Serializable;
import java.util.Date;

public class DebtSet implements Serializable, Comparable<DebtSet> {
    private String debtor;
    private String creditor;
    private Integer value;
    private Date date;
    private String place;
    private String description;
    private boolean active = true;

    public DebtSet(String creditor, Integer value, String debtor) {
        this.creditor = creditor;
        this.value = value;
        this.debtor = debtor;
    }

    public String getCreditor() {
        return creditor;
    }

    public Integer getValue() {
        return value;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int compareTo(DebtSet o) {
        //return o.getDate().compareTo(this.date);
        return o.getDate().compareTo(this.date);
    }
}
