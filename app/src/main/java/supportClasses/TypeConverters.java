package supportClasses;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

import Room.DebtSetStatuses;


public class TypeConverters {

    Gson gson = new Gson();
    private String TAG = TypeConverters.class.getSimpleName();
    @TypeConverter
    public ArrayList<DebtSet> jsonToArrayList(String json){
        Log.d(TAG, "jsonToArrayList");
        if (json == null) return new ArrayList<>();
        Type listType = new TypeToken<ArrayList<DebtSet>>(){}.getType();
        return gson.fromJson(json,listType);
    }

    @TypeConverter
    public String ArraylistToJson(ArrayList<DebtSet> debtSets){
        Log.d(TAG, "ArraylistToJson");
        return gson.toJson(debtSets);
    }

    @TypeConverter
    public DebtSetStatuses StringToDebtSetStatus(String debtStatusString){

        switch (debtStatusString){
            case "Resolved":
                return DebtSetStatuses.Resolved;
            case "Resolving":
                return DebtSetStatuses.Resolving;
            case "Unresolved":
                return DebtSetStatuses.Unresolved;
            default:
                return DebtSetStatuses.ErrorStatusUnrecognized;
        }
    }

    @TypeConverter
    public String DebtSetStatusToString(DebtSetStatuses debtSetStatus){
        return debtSetStatus.toString();
    }
}
