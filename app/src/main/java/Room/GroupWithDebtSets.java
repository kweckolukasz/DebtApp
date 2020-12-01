package Room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GroupWithDebtSets {
    @Embedded public DebtSet debtSet;
    @Relation(
            parentColumn = "groupId",
            entityColumn = "debtSetInGroupId"
    )
    public List<DebtSet> debtSetsInList;
}
