package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.debtapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import supportClasses.DebtSet;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtHolder> {

    private String TAG = DebtAdapter.class.getSimpleName();
    private ArrayList<DebtSet> allDebts = new ArrayList<>();
    private OnDebtItemListener onDebtItemListener;


    public DebtAdapter(OnDebtItemListener onDebtItemListener) {
        this.onDebtItemListener = onDebtItemListener;
    }

    @NonNull
    @Override
    public DebtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new DebtHolder(itemView, onDebtItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

            final DebtSet current = allDebts.get(position);
            holder.mCreditorTextView.setText(current.getCreditor()+" pożyczył ");
            holder.mAmountTextView.setText(String.valueOf(current.getValue())+" zł ");
            holder.mDebtorTextView.setText(current.getDebtor()+"owi");
            holder.mDeleteDebtImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: imageButton, debtSet: "+current.toString()+" date: "+current.getDate());
                    onDebtItemListener.onDeleteDebtClicked(current);
                }
            });


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

    class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnDebtItemListener onDebtItemListener;
        private TextView mCreditorTextView;
        private TextView mAmountTextView;
        private TextView mDebtorTextView;
        private ImageButton mDeleteDebtImageButton;

        public DebtHolder(@NonNull View itemView, OnDebtItemListener onDebtItemListener) {
            super(itemView);
            Log.d(TAG, "DebtHolder");
            mCreditorTextView = itemView.findViewById(R.id.creditor_textView);
            mAmountTextView = itemView.findViewById(R.id.amount_textView);
            mDebtorTextView = itemView.findViewById(R.id.debtor_textView);
            mDeleteDebtImageButton = itemView.findViewById(R.id.delete_debt_image_button);
            this.onDebtItemListener = onDebtItemListener;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: DebtHolder");

        }
    }

    public interface OnDebtItemListener {
        void onDeleteDebtClicked(DebtSet debtSet);
    }
}
