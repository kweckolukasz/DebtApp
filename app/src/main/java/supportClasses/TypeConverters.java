package supportClasses;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.room.TypeConverter;

import Room.DebtSetStatuses;


public class TypeConverters {

    private static DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static TimeZone timeZone = TimeZone.getTimeZone("Poland");

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

    @TypeConverter
    public Date StringToDate(String dateDB){
        if (dateDB != null){
            try{
                df.setTimeZone(timeZone);
                return df.parse(dateDB);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public String DateToString(Date date){
        df.setTimeZone(timeZone);
        return date == null ? null : df.format(date);
    }
}
