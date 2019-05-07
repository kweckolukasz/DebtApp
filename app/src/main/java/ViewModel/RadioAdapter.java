package ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debtapp.R;

import java.util.ArrayList;
import java.util.List;

import Room.Person;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.PersonHolder> {

    private List<Person> people = new ArrayList<>();
    private OnPersonRadioListener onPersonRadioListener;

    public RadioAdapter(OnPersonRadioListener onPersonRadioListener) {
        this.onPersonRadioListener = onPersonRadioListener;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);
        return new PersonHolder(itemView, onPersonRadioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Person current = people.get(position);
        holder.personTextView.setText(current.getName());
        holder.itemView.setId(current.getId());

    }

    @Override
    public int getItemCount() {
        return people.size();
    }


    class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPersonRadioListener onPersonRadioListener;
        private TextView personTextView;

        public PersonHolder(@NonNull View itemView, OnPersonRadioListener onPersonRadioListener) {
            super(itemView);
            personTextView = itemView.findViewById(R.id.item_person_name);
            this.onPersonRadioListener = onPersonRadioListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            onPersonRadioListener.onPersonRadioClick(getAdapterPosition());
            int position = getAdapterPosition();
            if (onPersonRadioListener != null && position != RecyclerView.NO_POSITION){
                Person person = people.get(position);
                onPersonRadioListener.onPersonRadioClick(person);

            }
        }
    }

    public interface OnPersonRadioListener {
        void onPersonRadioClick(Person person);
    }
}
