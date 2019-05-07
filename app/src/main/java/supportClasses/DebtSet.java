package supportClasses;

import java.io.Serializable;
import java.util.Date;

public class DebtSet implements Serializable {
    private String debtor;
    private String creditor;
    private Integer value;
    private Date date;
    private String place;

    public DebtSet(String creditor, Integer value, String debtor) {
        this.creditor = creditor;
        this.value = value;
        this.debtor = debtor;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public Integer getValue() {
        return value;
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

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }
}
