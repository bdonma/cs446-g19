package com.ba.cg.jn.tl.barter.TransactionAdapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ba.cg.jn.tl.barter.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private List<Transaction> transactionList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, dateCreated, value;
        public RelativeLayout container;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            title = (TextView) view.findViewById(R.id.title);
            dateCreated = (TextView) view.findViewById(R.id.dateCreated);
            value = (TextView) view.findViewById(R.id.value);
            container = view.findViewById(R.id.transaction_list_row_container);
        }

        @Override
        public void onClick(View v) {
            Log.d("swag", "tapped");
        }
    }

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setListContent(List<Transaction> transactionList){
        this.transactionList = transactionList;
        notifyItemRangeChanged(0, transactionList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.title.setText(transaction.getTitle());
        holder.dateCreated.setText(transaction.getDateCreated());
        holder.value.setText(transaction.getValue());

        if (transaction.getDoIOwe()) {
            holder.container.setBackgroundColor(Color.parseColor("#81C784"));
        } else {
            holder.container.setBackgroundColor(Color.parseColor("#E57373"));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
