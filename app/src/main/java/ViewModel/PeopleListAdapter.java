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

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.PersonListHolder> {

    private List<Person> people = new ArrayList<>();
    private OnPersonEditListener onPersonEditListener;

    public PeopleListAdapter(OnPersonEditListener onPersonEditListener) {
        this.onPersonEditListener = onPersonEditListener;
    }


    public void setPeople(List<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_item, parent, false);
        return new PersonListHolder(itemView, onPersonEditListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonListHolder holder, int position) {
        Person current = people.get(position);
        holder.nameTextView.setText(current.getName());
        holder.balanceTextView.setText(String.valueOf(current.getBalance()));
    }

    @Override
    public int getItemCount() {
        return people.size();
    }


    class PersonListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPersonEditListener onPersonEditListener;
        private TextView nameTextView;
        private TextView surNameTextView;
        private TextView balanceTextView;

        public PersonListHolder(@NonNull View itemView, OnPersonEditListener onPersonEditListener) {
            super(itemView);
//            personTextView = itemView.findViewById(R.id.item_person_name);
            nameTextView = itemView.findViewById(R.id.person_list_cardview_textView_name);
            surNameTextView = itemView.findViewById(R.id.person_list_cardview_textView_surName);
            balanceTextView = itemView.findViewById(R.id.person_list_cardview_textView_balance);
            this.onPersonEditListener = onPersonEditListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(onPersonEditListener != null && position != RecyclerView.NO_POSITION){
                onPersonEditListener.onPersonEditClick(people.get(position));
            }
        }

    }
    public interface OnPersonEditListener {

        void onPersonEditClick(Person person);
    }
    public void setOnPersonEditListener(OnPersonEditListener onPersonEditListener) {
        this.onPersonEditListener = onPersonEditListener;
    }
}

