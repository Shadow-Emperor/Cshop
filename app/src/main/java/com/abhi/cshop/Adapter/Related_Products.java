package com.abhi.cshop.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.model;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class Related_Products extends FirebaseRecyclerAdapter<model, Related_Products.myviewholder> {
    public Related_Products(FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(final Related_Products.myviewholder holder, int position, final model rec) {

        holder.pdtprice.setText(Globalvariable.currencySymbol + rec.getPrice());
        holder.pdtname.setText(rec.getProductName());
        Picasso.get().load(Uri.parse(rec.getImageURL())).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), product_main.class);
                intent.putExtra("pdtid", rec.getPdtid());
                view.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public Related_Products.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Related_Products.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_rec_pdt, parent, false));
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView pdtname;
        TextView pdtprice;


        public myviewholder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.rec_images);
            pdtname = (TextView) itemView.findViewById(R.id.rec_name);
            pdtprice = (TextView) itemView.findViewById(R.id.rec_price);


        }
    }

}