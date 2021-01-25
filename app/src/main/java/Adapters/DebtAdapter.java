package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.debtapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Room.DebtSet;

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
        holder.mCreditorTextView.setText(current.getCreditorId());
        holder.mAmountTextView.setText(String.valueOf(current.getValue()));
        holder.mDebtorTextView.setText(current.getDebtorId());
        holder.mDeleteDebtImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDebtItemListener.onDeleteDebtClicked(current);
            }
        });

        if (current.getDescription() != null) holder.mDescription.setText(current.getDescription());

        String dateDesc = "no date";
        Calendar date = Calendar.getInstance();
        date.setTime(current.getDate());
        long debtDate = current.getDate().getTime();
        long now = new Date().getTime();
        long diff = (now - debtDate)/60000;
        //int minutes = (int) TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MINUTES);
        int hours = (int) TimeUnit.HOURS.convert(diff, TimeUnit.MINUTES);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", new Locale("pl", "PL"));

        if (diff<60) {
            dateDesc = diff + " minut temu";
        }else if (hours>=1 && hours<=24){
            dateDesc = hours + " godzin temu";
        }else if (days>=1 && days<=7){
            switch (days) {
                case 1:
                    dateDesc = "wczoraj";
                    break;
                case 2:
                    dateDesc = "przedwczoraj";
                    break;
            }
        } else {
            dateDesc = sdf.format(debtDate);
        }

        holder.mDate.setText(dateDesc);


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount" + allDebts.size());
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
        private TextView mDescription;
        private TextView mDate;

        public DebtHolder(@NonNull View itemView, OnDebtItemListener onDebtItemListener) {
            super(itemView);
            Log.d(TAG, "DebtHolder");
            mCreditorTextView = itemView.findViewById(R.id.creditor_textView);
            mAmountTextView = itemView.findViewById(R.id.amount_textView);
            mDebtorTextView = itemView.findViewById(R.id.debtor_textView);
            mDeleteDebtImageButton = itemView.findViewById(R.id.delete_debt_image_button);
            mDescription = itemView.findViewById(R.id.history_item_desc);
            mDate = itemView.findViewById(R.id.history_item_date);
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
