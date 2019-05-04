package ViewModel;

import android.util.Log;
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
import supportClasses.DebtSet;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtHolder> {

    private String TAG = DebtAdapter.class.getSimpleName();
    private List<Person> people = new ArrayList<>();
    private ArrayList<DebtSet> allDebts = retrieveListOfDebts();

    private ArrayList<DebtSet> retrieveListOfDebts() {
        Log.d(TAG, "retrieveListOfDebts from people list - size: "+people.size());
        ArrayList<DebtSet> debtSets = new ArrayList<>();
        for (Person pe : people) {
            for (DebtSet ds : pe.getDebtSets()) {
                ds.setDebtor(pe.getName());
                debtSets.add(ds);
            }
        }
        return debtSets;
    }

    public void setPeople(List<Person> people) {
        Log.d(TAG, "setPeople");
        this.people = people;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DebtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.debt_item, parent, false);
        return new DebtHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        DebtSet current = allDebts.get(position);
        holder.mDebtorTextView.setText((String) current.getDebtor());
        holder.mAmountTextView.setText(String.valueOf(current.getValue()));
        holder.mCreditorTextView.setText((String) current.getName());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return allDebts.size();
    }

    class DebtHolder extends RecyclerView.ViewHolder {
        private TextView mDebtorTextView;
        private TextView mAmountTextView;
        private TextView mCreditorTextView;

        public DebtHolder(@NonNull View itemView) {

            super(itemView);
            Log.d(TAG, "DebtHolder");
            mDebtorTextView = itemView.findViewById(R.id.debtor_textView);
            mAmountTextView = itemView.findViewById(R.id.amount_textView);
            mCreditorTextView = itemView.findViewById(R.id.creditor_textView);
        }
    }
}
