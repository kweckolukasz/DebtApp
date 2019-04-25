package supportClasses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;



public class TypeConverters {

    Gson gson = new Gson();

    @TypeConverter
    public ArrayList<DebtSet> jsonToArrayList(String json){
        if (json == null) return new ArrayList<>();
        Type listType = new TypeToken<ArrayList<DebtSet>>(){}.getType();
        return gson.fromJson(json,listType);
    }

    @TypeConverter
    public String ArraylistToJson(ArrayList<DebtSet> debtSets){
        return gson.toJson(debtSets);
    }
}
