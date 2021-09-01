package com.abhi.cshop.Adapter;

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

import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.model;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class listAdapter extends FirebaseRecyclerAdapter<model, listAdapter.myviewholder> {
    public listAdapter(FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    public listAdapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new listAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_recycleview_list , parent, false));
    }


    @Override
        public void onBindViewHolder(final listAdapter.myviewholder holder, int position, final model model) {


            final boolean[] flag = {false};

            holder.pdprice.setText(Globalvariable.currencySymbol + model.getPrice());
            holder.pdname.setText(model.getProductName());
            Picasso.get().load(Uri.parse(model.getImageURL())).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.img1);

            holder.wishlist.setImageResource(R.drawable.ic_fav);


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Product Details").child("rating").child(model.getPdtid());

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
                    holder.listrating.setText(new DecimalFormat("#.#").format(total_rating));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                    String check="false";
                    intent.putExtra("pdname", model.getProductName());
                    intent.putExtra("pdprice", model.getPrice());
                    intent.putExtra("image", model.getImageURL().toString());
                    intent.putExtra("pdtid", model.getPdtid());
                    intent.putExtra("check","false");
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
                                DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                                HashMap<String, Object> pdtmap = new HashMap<>();
                                pdtmap.put("pdname", model.getProductName());
                                pdtmap.put("pdprice", model.getPrice());
                                pdtmap.put("image", model.getImageURL().toString());
                                pdtmap.put("pdtid", model.getPdtid());
                                DbRef.child("Users").child(user).child("cart").child("products").child(pdtkey).updateChildren(pdtmap);
                                Toast.makeText(v.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
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
                                    HashMap<String, Object> pdtmap = new HashMap<>();
                                    pdtmap.put("pdname", model.getProductName());
                                    pdtmap.put("pdprice", model.getPrice());
                                    pdtmap.put("image", model.getImageURL().toString());
                                    pdtmap.put("pdtid", model.getPdtid());
                                    DbRef.child("Users").child(user).child("wishlist").child("products").child(pdtkey).updateChildren(pdtmap);
                                    Toast.makeText(v.getContext(), "Add to wishlist", Toast.LENGTH_LONG);
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


        }


    public class myviewholder extends RecyclerView.ViewHolder {
        protected TextView pdname,pdprice,listrating;
        protected Button cart;
        protected ImageView heart,img1,wishlist;
        protected CardView card;

            public myviewholder(View itemView) {
                super(itemView);

                img1 = (ImageView) itemView.findViewById(R.id.pdt_image);
                pdname = (TextView) itemView.findViewById(R.id.pdt_name_list);
                pdprice = (TextView) itemView.findViewById(R.id.pdt_price_list);
                cart = (Button) itemView.findViewById(R.id.add_to_carthme);
                wishlist = (ImageView) itemView.findViewById(R.id.add_wishlist);
                card = (CardView)itemView.findViewById(R.id.card_view_list);
                listrating = (TextView) itemView.findViewById(R.id.list_rating);
                pdname.getText().toString();
                pdprice.getText().toString();
            }
        }

    }
