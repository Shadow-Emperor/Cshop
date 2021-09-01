package com.abhi.cshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.Checkout;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.model;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
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

public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {
    Context c;

    public myadapter(FirebaseRecyclerOptions<model> options,Context c) {
        super(options);
        this.c = c;
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, int position, final model model) {
        final boolean[] flag = {false};

        holder.pdprice.setText(Globalvariable.currencySymbol + model.getPrice());
        holder.pdname.setText(model.getProductName());
        holder.item_count.setText(String.valueOf(model.getStock())+" Available");
        holder.numberPicker.setMax((int)model.getStock());
        holder.numberPicker.setMin(0);
        holder.numberPicker.setUnit(1);
        holder.numberPicker.setValue(0);
        Picasso.get().load(Uri.parse(model.getImageURL()))
                .transform(new RoundedCornersTransformation(20,0))
                .into(holder.img1);
        holder.wishlist.setImageResource(R.drawable.ic_fav);

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Product Details").child("Products")
                .orderByChild("pdtid")
                .equalTo(model.getPdtid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ch_shot : snapshot.getChildren()) {
                    String pdtkey = ch_shot.getKey();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Cart").child("Users").child(user).child("wishlist");
                    ref.keepSynced(true);
                    ref.child("products").orderByChild("pdtid")
                            .equalTo(pdtkey)
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot cshot : snapshot.getChildren()) {
                                holder.wishlist.setImageResource(R.drawable.ic_favin);
                                flag[0] = true;
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


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), product_main.class);
                intent.putExtra("pdtid", model.getPdtid());
                view.getContext().startActivity(intent);
            }
        });
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseDatabase.getInstance().getReference("Product Details").child("Products").orderByChild("pdtid")
                        .equalTo(model.getPdtid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String pdtkey = childSnapshot.getKey();
                                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart")
                                        .child("Users").child(user).child("cart").child("products").child(pdtkey);
                                DbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long check = model.getStock();
                                        if (check!=0 && holder.numberPicker.getValue()!=0) {
                                            if (snapshot.exists()) {
                                                int stock = holder.numberPicker.getValue();
                                                HashMap<String, Object> cartmap = new HashMap<>();
                                                cartmap.put("stock" , stock);
                                                DbRef.updateChildren(cartmap);
                                                Snackbar snackbar = Snackbar.make(v , "Cart Updated" , Snackbar.LENGTH_LONG);
                                                View snackBarView = snackbar.getView();
                                                snackBarView.setBackground(c.getApplicationContext().getDrawable(R.drawable.snackbar));
                                                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                                                textView.setTextColor(v.getResources().getColor(R.color.teal_200));
                                                snackbar.show();
                                            } else {
                                                int stock = holder.numberPicker.getValue();
                                                HashMap<String, Object> cartmap = new HashMap<>();
                                                cartmap.put("pdname" , model.getProductName());
                                                cartmap.put("pdprice" , model.getPrice());
                                                cartmap.put("image" , model.getImageURL().toString());
                                                cartmap.put("pdtid" , model.getPdtid());
                                                cartmap.put("stock" , stock);
                                                DbRef.updateChildren(cartmap);
                                                Snackbar snackbar = Snackbar.make(v , "Added to cart" , Snackbar.LENGTH_LONG);
                                                View snackBarView = snackbar.getView();
                                                snackBarView.setBackground(c.getApplicationContext().getDrawable(R.drawable.snackbar));
                                                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                                                textView.setTextColor(v.getResources().getColor(R.color.teal_200));
                                                snackbar.show();
                                            }
                                        } else {
                                            Toast.makeText(v.getContext() , "No Product Available For Purchase" , Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });
        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (flag[0] == false) {
                    flag[0] = true;
                    holder.wishlist.setImageResource(R.drawable.ic_favin);
                    FirebaseDatabase.getInstance().getReference("Product Details").child("Products")
                            .orderByChild("pdtid")
                            .equalTo(model.getPdtid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String pdtkey = childSnapshot.getKey();

                                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                                HashMap<String, Object> wishlistmap = new HashMap<>();
                                wishlistmap.put("pdname", model.getProductName());
                                wishlistmap.put("pdprice", model.getPrice());
                                wishlistmap.put("image", model.getImageURL().toString());
                                wishlistmap.put("pdtid", model.getPdtid());
                                DbRef.child("Users").child(user).child("wishlist").child("products").child(pdtkey).updateChildren(wishlistmap);
                                Toast.makeText(v.getContext(), "Add to wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                } else{
                    holder.wishlist.setImageResource(R.drawable.ic_fav);
                FirebaseDatabase.getInstance().getReference("Product Details").child("Products").orderByChild("pdtid").equalTo(model.getPdtid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String pdtkey = childSnapshot.getKey();
                            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                            DbRef.child("Users").child(user).child("wishlist").child("products").child(pdtkey).removeValue();
                            flag[0]=false;
                            Toast.makeText(v.getContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
            }
        });
       holder.card.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
              if (holder.previewcheck==false){
                  holder.preview.setVisibility(View.VISIBLE);
                  holder.description.setText(model.getDescription());
                  
                  holder.previewcheck=true;
              }else{
                  holder.preview.setVisibility(View.GONE);
                  holder.previewcheck=false;
              }
               return false;
           }
       });
       holder.buy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
               FirebaseDatabase.getInstance().getReference("Cart")
                       .child("Users").child(user).child("cart").child("temppurchase").removeValue();

               HashMap<String, Object> map = new HashMap<>();
               map.put("pdtid" , model.getPdtid());
               map.put("total",model.getPrice());
               FirebaseDatabase.getInstance().getReference("Cart")
                       .child("Users").child(user).child("cart").child("temppurchase").child(model.getPdtid()).updateChildren(map);

               Intent intent = new Intent(v.getContext(), Checkout.class);
               Globalvariable.flag = true;
               v.getContext().startActivity(intent);
           }
       });
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.card_itemrecycle_home, parent, false));

    }

    protected class myviewholder extends RecyclerView.ViewHolder {
        protected TextView pdname,pdprice,description,item_count;
        protected Button cart,buy;
        protected ImageView heart,img1,wishlist,imageViewBackground;
        protected CardView card,preview;
        boolean previewcheck;
        private NumberPicker numberPicker;

        protected myviewholder(View itemView) {
            super(itemView);
            //imageViewBackground = itemView.findViewById(R.id.myimage);
            img1 = (ImageView) itemView.findViewById(R.id.iv_image);
            pdname = (TextView) itemView.findViewById(R.id.pdname);
            item_count = (TextView) itemView.findViewById(R.id.item_count);
            pdprice = (TextView) itemView.findViewById(R.id.pdprice);
            cart = (Button) itemView.findViewById(R.id.addtocarthme);
            wishlist = (ImageView) itemView.findViewById(R.id.wishlist);
            card = (CardView)itemView.findViewById(R.id.card_view);
            preview = itemView.findViewById(R.id.preview);
            description = itemView.findViewById(R.id.preview_description);
            buy = itemView.findViewById(R.id.preview_buy);
            previewcheck=false;
            pdname.getText().toString();
            pdprice.getText().toString();
            numberPicker = itemView.findViewById(R.id.number_picker);

        }
    }
}