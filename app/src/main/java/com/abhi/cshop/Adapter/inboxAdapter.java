package com.abhi.cshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Fragments.inboxFragment;
import com.abhi.cshop.R;
import com.abhi.cshop.model.history;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class inboxAdapter extends FirebaseRecyclerAdapter<history, inboxAdapter.myviewholder> {
    public inboxAdapter(FirebaseRecyclerOptions<history> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull inboxAdapter.myviewholder holder , int position , @NonNull history model) {

        holder.Date.setText(model.getDate());
        holder.NoofProducts.setText("Purchased "+String.valueOf(model.getNoofProducts())+"products for");
        holder.Price.setText("Rs. "+String.valueOf(model.getPrice())+"/-");
        holder.Deliverybtn.setVisibility(View.GONE);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @NonNull
    @Override
    public inboxAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        return new inboxAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_history, parent, false));

    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView Date,NoofProducts,Price,PurchaseId;
        CardView card;
        Button Deliverybtn;

        public myviewholder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card);
            Date = (TextView) itemView.findViewById(R.id.history_date);
            NoofProducts = (TextView) itemView.findViewById(R.id.history_count);
            Price = (TextView) itemView.findViewById(R.id.history_price);
            Deliverybtn = (Button) itemView.findViewById(R.id.Deliverybtn);
        }
    }
}
