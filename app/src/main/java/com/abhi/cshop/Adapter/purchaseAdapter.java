package com.abhi.cshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Fragments.purchase_History;
import com.abhi.cshop.R;
import com.abhi.cshop.model.history;
import com.abhi.cshop.model.historyDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class purchaseAdapter extends FirebaseRecyclerAdapter<history, purchaseAdapter.myviewholder> {
    public purchaseAdapter(FirebaseRecyclerOptions<history> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull purchaseAdapter.myviewholder holder , int position , @NonNull history model) {

        holder.Date.setText(model.getDate());
        holder.NoofProducts.setText("Purchased "+String.valueOf(model.getNoofProducts())+"products for");
        holder.Price.setText("Rs. "+String.valueOf(model.getPrice())+"/-");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_History.disp.setVisibility(View.VISIBLE);


                LinearLayoutManager lm =new LinearLayoutManager(v.getContext());
                lm.setStackFromEnd(true);
                lm.setReverseLayout(true);
                purchase_History.recycler_view_history_details.setLayoutManager(lm);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                historyDetailsAdapter purchaseadapter = new historyDetailsAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query) FirebaseDatabase.getInstance()
                        .getReference("Users").child(userid).child("purchasehistory").child(model.getPurchaseId()).child("purchaseproducts"), historyDetails.class).build());
                holder.detailsAdapter = purchaseadapter;
                purchase_History.recycler_view_history_details.setAdapter(purchaseadapter);
                holder.detailsAdapter.startListening();
                holder.detailsAdapter.notifyDataSetChanged();

            }
        });
        purchase_History.disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_History.disp.setVisibility(View.GONE);

            }
        });
    }

    @NonNull
    @Override
    public purchaseAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        return new purchaseAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_history, parent, false));

    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView Date,NoofProducts,Price;
        CardView card;
        historyDetailsAdapter detailsAdapter;

        public myviewholder(View itemView) {
            super(itemView);

            Date = (TextView) itemView.findViewById(R.id.history_date);
            NoofProducts = (TextView) itemView.findViewById(R.id.history_count);
            Price = (TextView) itemView.findViewById(R.id.history_price);
            card =(CardView) itemView.findViewById(R.id.card);

        }
    }
}
