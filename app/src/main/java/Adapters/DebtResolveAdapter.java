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

public class DebtResolveAdapter extends RecyclerView.Adapter<DebtResolveAdapter.DebtHolder> {

    private String TAG = DebtResolveAdapter.class.getSimpleName();
    private ArrayList<DebtSet> allMoneyFlow = new ArrayList<>();


    @NonNull
    @Override
    public DebtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.money_flow_item, parent, false);
        return new DebtHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        DebtSet current = allMoneyFlow.get(position);
        holder.mDebtorTextView.setText(current.getDebtor()+" musi oddać ");
        holder.mAmountTextView.setText(String.valueOf(current.getValue())+" zł ");
        holder.mCreditorTextView.setText(current.getCreditor()+"owi");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount"+ allMoneyFlow.size());
        return allMoneyFlow.size();
    }

    public void setAllMoneyFlow(ArrayList<DebtSet> debtSets) {
        Log.d(TAG, "setDebtSets");
        this.allMoneyFlow = debtSets;
        notifyDataSetChanged();
    }

    class DebtHolder extends RecyclerView.ViewHolder {
        private TextView mDebtorTextView;
        private TextView mAmountTextView;
        private TextView mCreditorTextView;

        public DebtHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "DebtHolder");
            mCreditorTextView = itemView.findViewById(R.id.creditor_resolve_textView);
            mAmountTextView = itemView.findViewById(R.id.amount_resolve_textView);
            mDebtorTextView = itemView.findViewById(R.id.debtor_resolve_textView);
        }
    }
}
