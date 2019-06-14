package Adapters;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debtapp.R;

import java.util.ArrayList;
import java.util.List;

import Room.Person;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CheckboxesAdapter extends RecyclerView.Adapter<CheckboxesAdapter.PersonHolder> {

    private List<Person> people = new ArrayList<>();
    private OnPeopleCheckboxesListener onPeopleCheckboxesListener;

    public CheckboxesAdapter(OnPeopleCheckboxesListener onPersonListener) {
        this.onPeopleCheckboxesListener = onPersonListener;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_people_item, parent, false);

        return new PersonHolder(itemView, onPeopleCheckboxesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Person current = people.get(position);
        holder.personName.setText(current.getName());
        if (!current.isActive()){
            holder.personName.setTextColor(Color.GRAY);
        } else {
            holder.personName.setTextColor(Color.BLACK);
        }
        holder.itemView.setId(current.getId());
        CardView cardView =(CardView) holder.itemView;
        TextView currentValue = cardView.findViewById(R.id.item_person_value);
        if (current.isCurrentDebtor()){
            cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.green));
            currentValue.setVisibility(View.VISIBLE);
            currentValue.setText(String.valueOf(current.getCurrentValue()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setElevation(20);
            }
        } else {
            cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
            currentValue.setVisibility(View.INVISIBLE);
            currentValue.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setElevation(6);
            }
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }


    class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPeopleCheckboxesListener onPeopleCheckboxesListener;
        private TextView personName;
        private TextView currentValue;

        public PersonHolder(@NonNull View itemView, OnPeopleCheckboxesListener onPersonListener) {
            super(itemView);
            personName = itemView.findViewById(R.id.item_person_name);
            currentValue = itemView.findViewById(R.id.item_person_value);
            this.onPeopleCheckboxesListener = onPersonListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(onPeopleCheckboxesListener != null && position != RecyclerView.NO_POSITION){
                Person person = people.get(position);
                onPeopleCheckboxesListener.onPersonCheckboxClick(person);
            }
        }
    }

    public interface OnPeopleCheckboxesListener {
        void onPersonCheckboxClick(Person person);
    }
}
