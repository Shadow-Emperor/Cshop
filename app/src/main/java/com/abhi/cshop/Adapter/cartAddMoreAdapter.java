package com.abhi.cshop.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Fragments.checkout_delivery;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class cartAddMoreAdapter extends FirebaseRecyclerAdapter<cart, cartAddMoreAdapter.myviewholder> {


    public cartAddMoreAdapter(FirebaseRecyclerOptions<cart> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, int position, final cart cart) {
        holder.pdtaddprice.setText(Globalvariable.currencySymbol + cart.getPdprice());
        holder.pdtaddname.setText(cart.getPdname());
        holder.numberpickercartview.setMax((int) cart.getStock());
        holder.numberpickercartview.setMin(1);
        holder.numberpickercartview.setUnit(1);
        holder.numberpickercartview.setValue(1);
        Picasso.get().load(Uri.parse(cart.getImage())).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.addimg);


        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Cart")
                .child("Users").child(user)
                .child("cart").child("temppurchase")
                .orderByChild("pdtid")
                .equalTo(cart.getPdtid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ch_shot : snapshot.getChildren()) {
                    String pdtkey = ch_shot.getKey();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Cart").child("Users").child(user).child("cart");
                    ref.keepSynced(true);
                    ref.child("products").orderByChild("pdtid")
                            .equalTo(pdtkey)
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot cshot : snapshot.getChildren()) {
                                        holder.addtobuyfromcheck.setChecked(true);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        holder.addtobuyfromcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addtobuyfromcheck.isChecked()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("pdtid" , cart.getPdtid());
                    map.put("total",cart.getPdprice());
                    map.put("count",holder.numberpickercartview.getValue());
                    FirebaseDatabase.getInstance().getReference("Cart")
                            .child("Users").child(user).child("cart").child("temppurchase").child(cart.getPdtid()).updateChildren(map);

                }else if ((holder.addtobuyfromcheck.isChecked())==false){

                    DatabaseReference databref = FirebaseDatabase.getInstance().getReference("Cart")
                            .child("Users").child(user).child("cart").child("temppurchase");
                    databref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                long price = (long) snapshot.child(cart.getPdtid()).child("total").getValue();
                                long stock = (long) snapshot.child(cart.getPdtid()).child("count").getValue();
                                long totalprice = price * stock;
                                Globalvariable.totalprice = Globalvariable.totalprice - totalprice;
                                checkout_delivery.totalPrice.setText("Rs. "+String.valueOf(Globalvariable.totalprice+"/-"));
                                databref.child(cart.getPdtid()).removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_add_view_checkout, parent, false));
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView addimg;
        TextView pdtaddname;
        TextView pdtaddprice;
        CheckBox addtobuyfromcheck;
        NumberPicker numberpickercartview;

        public myviewholder(View itemView) {
            super(itemView);

            addimg = (ImageView) itemView.findViewById(R.id.check_image_view);
            pdtaddname = (TextView) itemView.findViewById(R.id.check_name_view);
            pdtaddprice = (TextView) itemView.findViewById(R.id.check_number_view);
            addtobuyfromcheck = (CheckBox) itemView.findViewById(R.id.add_to_checkout);
            numberpickercartview =  itemView.findViewById(R.id.number_picker_cart_view);

        }
    }

}
