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

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonHolder> {

    private List<Person> people = new ArrayList<>();
    private OnPersonListener onPersonListener;

    public PeopleAdapter(OnPersonListener onPersonListener) {
        this.onPersonListener = onPersonListener;
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
        return new PersonHolder(itemView, onPersonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Person current = people.get(position);
        holder.personName.setText(current.getName());

    }

    @Override
    public int getItemCount() {
        return people.size();
    }


    class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPersonListener onPersonListener;
        private TextView personName;

        public PersonHolder(@NonNull View itemView, OnPersonListener onPersonListener) {
            super(itemView);
            personName = itemView.findViewById(R.id.item_person_name);
            this.onPersonListener = onPersonListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPersonListener.onPersonClick(getAdapterPosition());
        }
    }

    public interface OnPersonListener{
        void onPersonClick(int position);
    }
}
