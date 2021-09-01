package com.abhi.cshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Checkout;
import com.abhi.cshop.Fragments.CartFragment;
import com.abhi.cshop.Fragments.checkout_delivery;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.cart;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class cartAdapter extends FirebaseRecyclerAdapter<cart, cartAdapter.myviewholder> {
    public cartAdapter(FirebaseRecyclerOptions<cart> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final cartAdapter.myviewholder holder, int position, final cart cart) {

        holder.pricetext.setText(Globalvariable.currencySymbol + cart.getPdprice());
        holder.pdttext.setText(cart.getPdname());
        holder.cartnumberpicker.setMax((int)cart.getStock());
        holder.cartnumberpicker.setMin(1);
        holder.cartnumberpicker.setUnit(1);
        holder.cartnumberpicker.setValue(0);
        Picasso.get().load(Uri.parse(cart.getImage())).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.carimg);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("products");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.addtobuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.addtobuy.isChecked()) {
                            int stock = holder.cartnumberpicker.getValue();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("pdtid" , cart.getPdtid());
                            map.put("total",cart.getPdprice());
                            map.put("count",stock);
                            FirebaseDatabase.getInstance().getReference("Cart")
                                    .child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("temppurchase").child(cart.getPdtid()).updateChildren(map);

                        }else if ((holder.addtobuy.isChecked())==false){
                            FirebaseDatabase.getInstance().getReference("Cart")
                                    .child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("temppurchase").child(cart.getPdtid()).removeValue();

                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Product Details").child("rating").child(cart.getPdtid());

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
                    holder.pdt_rating_cart.setText(new DecimalFormat("#.#").format(total_rating));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.carimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check="true";
                Intent intent = new Intent(view.getContext(), product_main.class);
                intent.putExtra("pdname", cart.getPdname());
                intent.putExtra("pdprice", cart.getPdprice());
                intent.putExtra("image", cart.getImage().toString());
                intent.putExtra("pdtid", cart.getPdtid());
                intent.putExtra("check","true");
                view.getContext().startActivity(intent);
            }
        });
        holder.Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Checkout.class);
                v.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_recycleview_cart, parent, false));
    }


    public void deleteItem(int position) {
        ((DataSnapshot) getSnapshots().getSnapshot(position)).getRef().removeValue();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView carimg;
        TextView pdttext,pdt_rating_cart;
        TextView pricetext;
        Button Buy;
        CheckBox addtobuy;
        NumberPicker cartnumberpicker;
        int count;
        public myviewholder(View itemView) {
            super(itemView);

            carimg = (ImageView) itemView.findViewById(R.id.cart_image_view);
            pricetext = (TextView) itemView.findViewById(R.id.cart_name_view);
            pdttext = (TextView) itemView.findViewById(R.id.cart_number_view);
            Buy = (Button) itemView.findViewById(R.id.cart_buy_button);
            pdt_rating_cart = (TextView) itemView.findViewById(R.id.pdt_cart_rating);
            addtobuy = (CheckBox) itemView.findViewById(R.id.add_to_buy);
            cartnumberpicker = itemView.findViewById(R.id.cart_number_picker);


        }
    }

}