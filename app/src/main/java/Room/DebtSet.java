package Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "debt_sets",
        foreignKeys = {
            @ForeignKey(
                    entity = Person.class,
                    parentColumns = "personId",
                    childColumns = "debtorId"
            ),
            @ForeignKey(
                    entity = Person.class,
                    parentColumns = "personId",
                    childColumns = "creditorId"
            ),
                @ForeignKey(
                        entity = Group.class,
                        parentColumns = "groupId",
                        childColumns = "groupId"
                )
        })
public class DebtSet {


    @PrimaryKey
    private int debtSetid;

    private int debtorId;

    private int creditorId;

    private int debtSetInGroupId;

    private int value;

    private DebtSetStatuses status;

    private boolean isActive;



    public int getDebtSetid() {
        return debtSetid;
    }

    public void setDebtSetid(int debtSetid) {
        this.debtSetid = debtSetid;
    }

    public int getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(int debtorId) {
        this.debtorId = debtorId;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(int creditorId) {
        this.creditorId = creditorId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DebtSetStatuses getStatuses() {
        return status;
    }

    public void setStatuses(DebtSetStatuses status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
