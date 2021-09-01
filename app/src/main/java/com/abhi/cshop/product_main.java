package com.abhi.cshop;


import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.abhi.cshop.Adapter.Related_Products;
import com.abhi.cshop.Adapter.comment_adapter;
import com.abhi.cshop.Adapter.product_images_adapter;
import com.abhi.cshop.Fragments.comment_fragment;
import com.abhi.cshop.Fragments.uploadimagesfragment;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.comment_model;
import com.abhi.cshop.model.model;
import com.abhi.cshop.model.modelimages;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class product_main extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button buy,add_to_cart;
    boolean flag;
    private ImageView back_btn,pdt_wish,image_pdt,pdt_background,zoomimage,addimages;
    private String pdtid,user,name,description,uri;
    private long price;
    private TextView textname,txtversion,textprice,textpriceorgin,textdescription,comment_seemore,max_rev_rating,checkstock;
    private product_images_adapter Product_Images_Adapter;
    private comment_adapter Comment_Adapter;
    private Related_Products rec_adapter0,rec_adapter1,rec_adapter2;
    private RecyclerView imagerecview,rec_pdt0,rec_pdt1,rec_pdt2,rec_cmt;
    private LinearLayoutManager linearlayout;
    private RelativeLayout image_full_size;
    private int count,review_count;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,dbref,databaseRef;
    private ProgressBar progress1,progress2,progress3,progress4,progress5;
    private int progressStatus = 0;
    private TextView ratingcount1,ratingcount2,ratingcount3,ratingcount4,ratingcount5,rating_total;
    private Handler handler = new Handler();
    long ratingmax,rating_valuedb;
    boolean zoomflag = false;
    private NumberPicker numberpickerproduct;
    private Uri temp;
    private RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_main);

        textname = (TextView) findViewById(R.id.pdt_name);
        textprice = (TextView) findViewById(R.id.pdt_price);
        textpriceorgin = (TextView) findViewById(R.id.pdt_price_orgin);
        textdescription =(TextView) findViewById(R.id.pdt_description);
        add_to_cart = (Button) findViewById(R.id.pdt_add_to_cart);
        buy = (Button) findViewById(R.id.buy_pdt);
        comment_seemore = (TextView)findViewById(R.id.comment_see_more);
        pdtid = getIntent().getExtras().get("pdtid").toString();
        image_pdt = (ImageView) findViewById(R.id.image_product_view);
        addimages = (ImageView) findViewById(R.id.add_images);
        max_rev_rating = (TextView) findViewById(R.id.max_review_rating);
        txtversion = (TextView) findViewById(R.id.pdt_version);
        numberpickerproduct = (NumberPicker) findViewById(R.id.number_picker_product);
        checkstock = (TextView) findViewById(R.id.check_stock);
        flag=false;
        FirebaseAuth instance = FirebaseAuth.getInstance();
        auth = instance;
        user = instance.getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Product Details").child("rating").child(pdtid);
        dbref=firebaseDatabase.getReference().child("Product Details").child("comment").child(pdtid);
        databaseRef=firebaseDatabase.getReference().child("Product Details").child("rating").child("user").child(pdtid);
        getdata();

       // price =  textprice.getText();
        image_full_size = findViewById(R.id.image_full_size);
        zoomimage = findViewById(R.id.zoomimage);

        image_full_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom(temp);
            }
        });

        addimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("pdtid", pdtid );
                Fragment fragInfo = new uploadimagesfragment();
                fragInfo.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_bottom,R.anim.enter_bottom,R.anim.exit_top);
                transaction.replace(R.id.comment_fragment_container, fragInfo);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        pdt_background = findViewById(R.id.background_product);
        int[] images =  {R.drawable.oval_copy,R.drawable.oval_background ,R.drawable.oval_background2 ,R.drawable.oval_background3};

        Random random = new Random();
        Picasso.get().load(images[random.nextInt(images.length)]).fit().into(pdt_background);

        back_btn = (ImageView) findViewById(R.id.back_btn);
        pdt_wish= findViewById(R.id.pdt_wishlist);

        progress5 = findViewById(R.id.rating_progress_5);
        ratingcount5 = (TextView) findViewById(R.id.rating_count_5);
        progress4 = findViewById(R.id.rating_progress_4);
        ratingcount4 = (TextView) findViewById(R.id.rating_count_4);
        progress3 = findViewById(R.id.rating_progress_3);
        ratingcount3 = (TextView) findViewById(R.id.rating_count_3);
        progress2 = findViewById(R.id.rating_progress_2);
        ratingcount2 = (TextView) findViewById(R.id.rating_count_2);
        progress1 = findViewById(R.id.rating_progress_1);
        ratingcount1 = (TextView) findViewById(R.id.rating_count_1);
        rating_total =(TextView) findViewById(R.id.rating_total);
        rating =(RatingBar) findViewById(R.id.ratingBar);

        /*rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(product_main.this, String.valueOf(rating), Toast.LENGTH_SHORT).show();
            }
        });*/

        ImageView mImgCheck = (ImageView) findViewById(R.id.ratingsubmit);
        mImgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) mImgCheck.getDrawable()).start();
            }
        });
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(user)) {
                    rating.setRating((long) snapshot.child(user).getValue());
                    rating_valuedb = (long) snapshot.child(user).getValue();

                    ((Animatable) mImgCheck.getDrawable()).start();
                }
                mImgCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rating.getRating()!=0) {
                            long ratingvalue = (long) rating.getRating();
                            ((Animatable) mImgCheck.getDrawable()).start();
                           if (ratingvalue!=rating_valuedb) {
                               ratingupdate(ratingvalue);
                           }
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long star5 = (long) snapshot.child("star_5").getValue();
                            long star4 = (long) snapshot.child("star_4").getValue();
                            long star3 = (long) snapshot.child("star_3").getValue();
                            long star2 = (long) snapshot.child("star_2").getValue();
                            long star1 = (long) snapshot.child("star_1").getValue();
                            ratingmax = star1 + star2 + star3 + star4 + star5;

                            rating(progress5 , ratingcount5 , star5 , ratingmax);

                            rating(progress4 , ratingcount4 , star4 , ratingmax);

                            rating(progress3 , ratingcount3 , star3 , ratingmax);

                            rating(progress2 , ratingcount2 , star2 , ratingmax);

                            rating(progress1 , ratingcount1 , star1 , ratingmax);
                            float star_5=star5;
                            float star_4=star4;
                            float star_3=star3;
                            float star_2=star2;
                            float star_1=star1;
                            float rating_count = (star_5*5+ star_4*5+ star_3*3+ star_2*2+ star_1*1)/ratingmax;
                            rating_total.setText(new DecimalFormat("#.#").format(rating_count));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        review_count = (int)snap.getChildrenCount();
                        max_rev_rating.setText(ratingmax+" ratings & "+review_count+" reviews");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




/**********image recicleview***********/
        imagerecview = (RecyclerView)findViewById(R.id.recycle_img);
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Product Details");
        mDatabaseRef.child("Images").child(pdtid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count= (int)dataSnapshot.getChildrenCount();
                if (count>=1) {
                    linearlayout = new GridLayoutManager(product_main.this , count);
                    imagerecview.setLayoutManager(linearlayout);
                    product_images_adapter pdtadapter = new product_images_adapter(new FirebaseRecyclerOptions.Builder().setQuery((Query)
                                    FirebaseDatabase.getInstance().getReference().child("Product Details").child("Images").child(pdtid)
                            , modelimages.class).build() , product_main.this);
                    Product_Images_Adapter = pdtadapter;
                    imagerecview.setAdapter(pdtadapter);
                    Product_Images_Adapter.startListening();
                    Product_Images_Adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products").child(pdtid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    long stock = (long) snapshot.child("stock").getValue();
                                    if (stock!=0 && numberpickerproduct.getValue()!=0){
                                        cart(v);
                                    } else {
                                        Toast.makeText(product_main.this , "No Product Available For Purchase" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Cart").child("Users").child(user).child("wishlist");
        ref.keepSynced(true);
        ref.child("products").orderByChild("pdtid")
                .equalTo(pdtid)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot cshot : snapshot.getChildren()) {
                            //String wishkey = cshot.getKey();
                            pdt_wish.setImageResource(R.drawable.ic_favin);
                            flag = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });


        pdt_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (flag == false) {
                    flag = true;
                    pdt_wish.setImageResource(R.drawable.ic_favin);

                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart");

                    HashMap<String, Object> wishlistmap = new HashMap<>();
                    wishlistmap.put("pdname", name);
                    wishlistmap.put("description", description);
                    wishlistmap.put("pdprice",price);
                    wishlistmap.put("image",uri);
                    wishlistmap.put("pdtid", pdtid);
                    DbRef.child("Users").child(user).child("wishlist").child("products").child(pdtid).updateChildren(wishlistmap);
                    Toast.makeText(v.getContext(), "Add to wishlist", Toast.LENGTH_LONG).show();

                } else{
                    pdt_wish.setImageResource(R.drawable.ic_fav);
                    DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                    DbRef.child("Users").child(user).child("wishlist").child("products").child(pdtid).removeValue();
                    flag=false;
                    Toast.makeText(v.getContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                }


            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products").child(pdtid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    long stock = (long) snapshot.child("stock").getValue();
                                    if (stock!=0 && numberpickerproduct.getValue()!=0){
                                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        FirebaseDatabase.getInstance().getReference("Cart")
                                                .child("Users").child(user).child("cart").child("temppurchase").removeValue();

                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("pdtid" , pdtid);
                                        map.put("total",price);
                                        map.put("count",numberpickerproduct.getValue());
                                        FirebaseDatabase.getInstance().getReference("Cart")
                                                .child("Users").child(user).child("cart").child("temppurchase").child(pdtid).setValue(map);

                                        Intent intent = new Intent(product_main.this,Checkout.class);
                                        Globalvariable.flag = true;
                                        Globalvariable.totalprice = 0;
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(product_main.this , "No Product Available For Purchase" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        /***********comment recview****************/
        rec_cmt = (RecyclerView)findViewById(R.id.rv_comment);
        linearlayout = new LinearLayoutManager(product_main.this);
        rec_cmt.setLayoutManager(linearlayout);
        comment_adapter c_adapter = new comment_adapter(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Product Details").child("comment").child(pdtid)
                .limitToFirst(2), comment_model.class).build());
        Comment_Adapter = c_adapter;
        rec_cmt.setAdapter(c_adapter);
        Comment_Adapter.startListening();
        Comment_Adapter.notifyDataSetChanged();

        int[] i =  {1,2,3};
        Random ran = new Random();
        int a =i[ran.nextInt(i.length)];


        switch (a){
            case 1:
                rec_rec0();
                break;
            case 2:
                rec_rec0();
                rec_rec1();
                break;
            case 3:
                rec_rec0();
                rec_rec1();
                rec_red2();
                break;
        }

        comment_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("pdtid", pdtid );
                Fragment fragInfo = new comment_fragment();
                fragInfo.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_top,R.anim.exit_bottom,R.anim.enter_bottom,R.anim.exit_top);
                transaction.replace(R.id.comment_fragment_container, fragInfo);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
    }




    private void getdata() {
        FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products").child(pdtid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        textname.setText(dataSnapshot.child("productName").getValue().toString());
                        textprice.setText(Globalvariable.currencySymbol+dataSnapshot.child("price").getValue());
                        textpriceorgin.setText(Globalvariable.currencySymbol+dataSnapshot.child("price").getValue().toString());
                        textpriceorgin.setPaintFlags(textpriceorgin.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        textdescription.setText(dataSnapshot.child("description").getValue().toString());
                        txtversion.setText(dataSnapshot.child("version").getValue().toString());
                        long stock = (long) dataSnapshot.child("stock").getValue();
                        numberpickerproduct.setMax((int)stock);
                        numberpickerproduct.setMin(0);
                        numberpickerproduct.setUnit(1);
                        numberpickerproduct.setValue(0);
                        if (stock==0){
                            checkstock.setText("out of Stock ");
                        }else {
                            checkstock.setText(String.valueOf(stock)+" in Stock ");
                        }

                        Picasso.get().load(Uri.parse(dataSnapshot.child("imageURL").getValue().toString())).into(image_pdt);

                        name =dataSnapshot.child("productName").getValue().toString();
                        price= (long) dataSnapshot.child("price").getValue();
                        description= dataSnapshot.child("description").getValue().toString();
                        uri = dataSnapshot.child("imageURL").getValue().toString();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    /*******************recomended recview*******************/
    private void rec_rec0() {
        /******1*****/
        rec_pdt0 = (RecyclerView)findViewById(R.id.related_products0);
        linearlayout =new GridLayoutManager(product_main.this,10);
        rec_pdt0.setLayoutManager(linearlayout);
        Related_Products rel_product0 = new Related_Products(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products")
                , model.class).build());
        rec_adapter0 =rel_product0;
        rec_pdt0.setAdapter(rel_product0);
        rec_adapter0.startListening();
        rec_adapter0.notifyDataSetChanged();
    }

    private void rec_rec1() {
        /*******2*******/
        rec_pdt1 = (RecyclerView)findViewById(R.id.related_products1);
        linearlayout =new GridLayoutManager(product_main.this,10);
        rec_pdt1.setLayoutManager(linearlayout);
        Related_Products rel_product1 = new Related_Products(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products")
                ,model.class).build());
        rec_adapter1 =rel_product1;
        rec_pdt1.setAdapter(rel_product1);
        rec_adapter1.startListening();
        rec_adapter1.notifyDataSetChanged();
    }

    private void rec_red2() {

        /********3*******/
        rec_pdt2 = (RecyclerView)findViewById(R.id.related_products2);
        linearlayout =new GridLayoutManager(product_main.this,10);
        rec_pdt2.setLayoutManager(linearlayout);
        Related_Products rel_product2 = new Related_Products(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products")
                ,model.class).build());
        rec_adapter2 =rel_product2;
        rec_pdt2.setAdapter(rel_product2);
        rec_adapter2.startListening();
        rec_adapter2.notifyDataSetChanged();
    }

    public void cart(View v) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product Details");
        databaseReference.child("Products").orderByChild("pdtid").equalTo(pdtid).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String pdtkey = childSnapshot.getKey();
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Cart")
                            .child("Users").child(user).child("cart").child("products").child(pdtkey);
                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int stock = numberpickerproduct.getValue();
                                HashMap<String, Object> cartmap = new HashMap<>();
                                cartmap.put("stock" , stock);
                                databaseRef.updateChildren(cartmap);
                                Snackbar sbar = Snackbar.make(v,"Cart updated",Snackbar.LENGTH_LONG);
                                View sBarView = sbar.getView();
                                sBarView.setBackground(getApplication().getApplicationContext().getDrawable(R.drawable.snackbar));
                                TextView textView = (TextView) sBarView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(getApplication().getResources().getColor(R.color.black));
                                sbar.show();

                            }else{
                                HashMap<String, Object> cartmap = new HashMap<>();
                                int stock = numberpickerproduct.getValue();
                                cartmap.put("pdname" , name);
                                cartmap.put("pdprice" , price);
                                cartmap.put("image" , uri);
                                cartmap.put("pdtid" , pdtid);
                                cartmap.put("description" , description);
                                cartmap.put("stock" , stock);
                                databaseRef.updateChildren(cartmap);
                                Snackbar snackbar = Snackbar.make(v,"Added to cart" ,Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackground(getApplication().getApplicationContext().getDrawable(R.drawable.snackbar));
                                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(getApplication().getResources().getColor(R.color.teal_200));
                                snackbar.show();                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Product_Images_Adapter != null) {
            Product_Images_Adapter.startListening();
            Product_Images_Adapter.notifyDataSetChanged();

        }

    }

    public void zoom(Uri zoom) {
        if(zoomflag ==false) {
            Picasso.get().load(zoom).into(zoomimage);
            temp = zoom;
            Transition transition = new Fade();
            transition.setDuration(600);
            transition.addTarget(image_full_size);
            TransitionManager.beginDelayedTransition(image_full_size,transition);
            image_full_size.setVisibility(View.VISIBLE);
            zoomflag = true;
        }else{
            Transition transition = new Fade();
            transition.setDuration(400);
            transition.addTarget(image_full_size);
            TransitionManager.beginDelayedTransition(image_full_size,transition);
            image_full_size.setVisibility(View.INVISIBLE);
            zoomflag = false;
        }

    }

    private void rating(ProgressBar progressbar , TextView ratingcount , long star , long rating) {
        progressbar.setMax((int)rating);
        progressStatus =(int)star;
        progressbar.setProgress(progressStatus);
        ratingcount.setText(progressStatus+"");

    }

    private void ratingupdate(long ratingvalue) {

        long value_db[]=new long[5];
        value_db[0] = 0;
        value_db[1] = 0;
        value_db[2] = 0;
        value_db[3] = 0;
        value_db[4] = 0;

        long value[]=new long[5];
        value[0]=0;
        value[1]=0;
        value[2]=0;
        value[3]=0;
        value[4]=0;
        value[(int)ratingvalue-1]=1;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String childname;
                for (int i=0;i<value_db.length;i++) {
                    int num = i + 1;
                    childname = "star_" + num;
                    if (snapshot.exists()) {
                        value_db[i] = (long) snapshot.child(childname).getValue();
                    }else value_db[i] = 0;
                }
                HashMap<String, Object> ratingmap = new HashMap<>();
                ratingmap.put(user , ratingvalue);
                databaseRef.updateChildren(ratingmap);
                HashMap<String, Object> a = new HashMap<>();
                for (int i=0;i<value.length;i++){
                    int num = i + 1;
                    childname = "star_" + num;
                    if (i+1==rating_valuedb){
                        value[i]=-1;
                    }
                    a.put(childname , value_db[i] + value[i]);
                    databaseReference.updateChildren(a);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}