package com.abhi.cshop.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Fragments.checkout_delivery;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.checkoutProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class checkoutProductsAdapter extends FirebaseRecyclerAdapter<checkoutProduct, checkoutProductsAdapter.myviewholder> {
    public checkoutProductsAdapter(FirebaseRecyclerOptions<checkoutProduct> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder , int position , @NonNull checkoutProduct model) {
        holder.checkcountview.setText("x "+model.getCount());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        if (Globalvariable.flag==true){
            holder.cancel.setVisibility(View.INVISIBLE);
            DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products");
            dbreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChild(model.getPdtid())) {
                            holder.pdt_name_check.setText((CharSequence) snapshot.child(model.getPdtid()).child("productName").getValue());
                            long price = (long) snapshot.child(model.getPdtid()).child("price").getValue();
                            long totalprice = price * model.getCount();
                            holder.pdt_price_check.setText(String.valueOf(String.valueOf(totalprice)));
                            Picasso.get().load(Uri.parse(String.valueOf(snapshot.child(model.getPdtid()).child("imageURL").getValue())))
                                    .transform(new RoundedCornersTransformation(20 , 0)).fit().into(holder.pdt_image_check);
                            long temp = totalprice;
                            Globalvariable.totalprice = Globalvariable.totalprice + temp;
                            checkout_delivery.totalPrice.setText("Rs. "+String.valueOf(Globalvariable.totalprice+"/-"));
                            Globalvariable.pdtlist.add(model.getPdtid());
                            Globalvariable.count.add(model.getCount());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else if (Globalvariable.flag==false){
            DatabaseReference Databaseref = FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(auth.getCurrentUser()
                    .getUid()).child("cart").child("products");
            Databaseref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChild(model.getPdtid())) {
                            holder.pdt_name_check.setText((CharSequence) snapshot.child(model.getPdtid()).child("pdname").getValue());
                            long price = (long) snapshot.child(model.getPdtid()).child("pdprice").getValue();
                            long totalprice = price * model.getCount();
                            holder.pdt_price_check.setText(String.valueOf(totalprice));
                            Picasso.get().load(Uri.parse(String.valueOf(snapshot.child(model.getPdtid()).child("image").getValue())))
                                    .transform(new RoundedCornersTransformation(20 , 0)).fit().into(holder.pdt_image_check);
                            long temp = totalprice;
                            Globalvariable.totalprice = Globalvariable.totalprice + temp;
                            checkout_delivery.totalPrice.setText("Rs. "+String.valueOf(Globalvariable.totalprice+"/-"));
                            Globalvariable.pdtlist.add(model.getPdtid());
                            Globalvariable.count.add(model.getCount());

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        DatabaseReference DbRef = FirebaseDatabase.getInstance()
                .getReference("Cart").child("Users").child(auth.getCurrentUser()
                        .getUid()).child("cart").child("temppurchase");
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            DatabaseReference databref = FirebaseDatabase.getInstance()
                                    .getReference("Cart").child("Users").child(auth.getCurrentUser()
                                            .getUid()).child("cart").child("temppurchase").child(model.getPdtid());
                            databref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snap) {
                                    if (snap.exists()) {

                                        long price = (long) snap.child("total").getValue();
                                        long temp =  (long) snap.child("count").getValue();
                                        long totalprice = price * temp;
                                        Globalvariable.totalprice = Globalvariable.totalprice - totalprice;
                                        checkout_delivery.totalPrice.setText("Rs. " + String.valueOf(Globalvariable.totalprice + "/-"));
                                        DbRef.child(model.getPdtid()).removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    @Override
    public checkoutProductsAdapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new checkoutProductsAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_view, parent, false));
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView pdt_image_check;
        TextView pdt_name_check,pdt_price_check,checkcountview;
        Button cancel;

        public myviewholder(View itemView) {
            super(itemView);

            pdt_image_check = (ImageView) itemView.findViewById(R.id.product_list_image);
            pdt_name_check = (TextView) itemView.findViewById(R.id.product_list_name);
            pdt_price_check = (TextView) itemView.findViewById(R.id.product_list_price);
            checkcountview = (TextView) itemView.findViewById(R.id.check_count_view);
            cancel = (Button) itemView.findViewById(R.id.remove_pdt);

        }
    }

}
