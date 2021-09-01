package com.abhi.cshop.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Fragments.purchase_History;
import com.abhi.cshop.R;
import com.abhi.cshop.model.history;
import com.abhi.cshop.model.historyDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class historyDetailsAdapter extends FirebaseRecyclerAdapter<historyDetails, historyDetailsAdapter.myviewholder> {
    public historyDetailsAdapter(FirebaseRecyclerOptions<historyDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull historyDetailsAdapter.myviewholder holder , int position , @NonNull historyDetails model) {
        holder.history_pdtname.setText(model.getName());
        holder.history_price.setText("Rs. "+String.valueOf(model.getPrice())+"/-");
        holder.history_count.setText(String.valueOf(model.getCount()));
        Picasso.get().load(Uri.parse(model.getImage())).into(holder.history_image);
        purchase_History.disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_History.disp.setVisibility(View.GONE);

            }
        });
    }

    @NonNull
    @Override
    public historyDetailsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        return new historyDetailsAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_details_view, parent, false));

    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView history_pdtname,history_count,history_price;
        ImageView history_image;

        public myviewholder(View itemView) {
            super(itemView);

            history_pdtname = (TextView) itemView.findViewById(R.id.history_pdtname);
            history_count = (TextView) itemView.findViewById(R.id.history_count);
            history_price = (TextView) itemView.findViewById(R.id.history_price);
            history_image = (ImageView) itemView.findViewById(R.id.history_image);


        }
    }
}