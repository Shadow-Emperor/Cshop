package com.abhi.cshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.Product_model;
import com.abhi.cshop.product_main;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder>{

    private Context mContext;
    private List<Product_model> product;
    private boolean isFargment;


    public productAdapter(Context mContext, List<Product_model> product, boolean isFargment) {
        this.mContext = mContext;
        this.product = product;
        this.isFargment = isFargment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item , parent , false);
        return new productAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final Product_model mproduct = product.get(position);

        holder.search_productname.setText(mproduct.getProductName());
        holder.search_price.setText(Globalvariable.currencySymbol+mproduct.getPrice());

        Picasso.get().load(mproduct.getImageURL()).placeholder(R.mipmap.ic_launcher)
                .transform(new RoundedCornersTransformation(20,0))
                .fit().into(holder.searchviewimage);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Product Details").child("rating").child(mproduct.getPdtid());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long star5 = (long) snapshot.child("star_5").getValue();
                            long star4 = (long) snapshot.child("star_4").getValue();
                            long star3 = (long) snapshot.child("star_3").getValue();
                            long star2 = (long) snapshot.child("star_2").getValue();
                            long star1 = (long) snapshot.child("star_1").getValue();
                            float ratingmax = star1 + star2 + star3 + star4 + star5;

                            float star_5 = star5;
                            float star_4 = star4;
                            float star_3 = star3;
                            float star_2 = star2;
                            float star_1 = star1;
                            float total_rating =(star_5 * 5 + star_4 * 5 + star_3 * 3 + star_2 * 2 + star_1 * 1) / ratingmax;

                            //holder.searchrating.setText(String.valueOf(total_rating));
                            holder.searchrating.setText(new DecimalFormat("#.#").format(total_rating));

                            holder.ratingbar.setRating(total_rating);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.btn_add_to_cart_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), product_main.class);
                intent.putExtra("pdtid", mproduct.getPdtid());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView searchviewimage;
        private TextView search_productname,searchrating;
        private TextView search_price;
        private CardView btn_add_to_cart_search;
        private RatingBar ratingbar;


        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            searchviewimage = itemView.findViewById(R.id.search_image_view);
            search_productname = itemView.findViewById(R.id.search_productname);
            search_price = itemView.findViewById(R.id.search_price);
            btn_add_to_cart_search = itemView.findViewById(R.id.btn_add_to_cart_search);
            ratingbar = itemView.findViewById(R.id.search_rating_bar);
            searchrating = itemView.findViewById(R.id.search_rating);

        }
    }


}