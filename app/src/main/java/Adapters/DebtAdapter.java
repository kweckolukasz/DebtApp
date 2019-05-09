package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debtapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtHolder> {

    private String TAG = DebtAdapter.class.getSimpleName();
    private ArrayList<DebtSet> allDebts = new ArrayList<>();


    @NonNull
    @Override
    public DebtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new DebtHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        DebtSet current = allDebts.get(position);
        holder.mCreditorTextView.setText(current.getCreditor()+" pożyczył ");
        holder.mAmountTextView.setText(String.valueOf(current.getValue())+" zł ");
        holder.mDebtorTextView.setText(current.getDebtor()+"owi");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount"+allDebts.size());
        return allDebts.size();
    }

    public void setDebtSets(ArrayList<DebtSet> debtSets) {
        Log.d(TAG, "setDebtSets");
        this.allDebts = debtSets;
        notifyDataSetChanged();
    }

    class DebtHolder extends RecyclerView.ViewHolder {
        private TextView mCreditorTextView;
        private TextView mAmountTextView;
        private TextView mDebtorTextView;

        public DebtHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "DebtHolder");
            mCreditorTextView = itemView.findViewById(R.id.creditor_textView);
            mAmountTextView = itemView.findViewById(R.id.amount_textView);
            mDebtorTextView = itemView.findViewById(R.id.debtor_textView);
        }
    }
}
