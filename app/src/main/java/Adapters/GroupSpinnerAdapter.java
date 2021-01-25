package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.debtapp.R;

import java.util.ArrayList;
import java.util.List;

import Room.Group;

public class GroupSpinnerAdapter extends ArrayAdapter<Group> {


    public GroupSpinnerAdapter(@NonNull Context context, @NonNull List<Group> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.groups_spinner_item, parent, false);
        }
        TextView groupNameTextView = convertView.findViewById(R.id.groupNameTextView);
        Group currentGroup = getItem(position);
        if (currentGroup != null) {
            groupNameTextView.setText(currentGroup.getName());
        }

        currentGroup.setActive(true);
        return convertView;
    }
}
