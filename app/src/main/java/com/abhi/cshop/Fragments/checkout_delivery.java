package com.abhi.cshop.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.Adapter.cartAddMoreAdapter;
import com.abhi.cshop.Adapter.checkoutProductsAdapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.cart;
import com.abhi.cshop.model.checkoutProduct;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.alterac.blurkit.BlurLayout;

public class checkout_delivery extends Fragment {

    private CardView placeorder;
    private ViewPager viewPager;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private checkoutProductsAdapter checkoutproductsadapter;
    private EditText new_address,payusername;
    private TextView address_new,item_count_checkout,gift_wrap_label,location;
    public static TextView totalPrice;
    cartAddMoreAdapter adapter;
    BlurLayout blurLayout;
    Spinner state;
    public Fragment selectorFragment;
    private Button change_address,new_address_change,checkout_add_pdt;
    RelativeLayout change_to_new_address,addToBuyLayout,gift_wrap;
    RecyclerView recyclerView,addToBuyrecview;
    boolean check=true;
    public checkout_delivery() {
    }


    public static checkout_delivery newInstance(String param1 , String param2) {
        checkout_delivery fragment = new checkout_delivery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1 , param1);
        args.putString(ARG_PARAM2 , param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout_delivery , container , false);
        placeorder = view.findViewById(R.id.palce_order);
        change_to_new_address = view.findViewById(R.id.change_to_new_address);
        change_address = view.findViewById(R.id.change_address);
        blurLayout = view.findViewById(R.id.blurLayout);
        address_new = (TextView) view.findViewById(R.id.address_new);
        new_address = (EditText) view.findViewById(R.id.new_address);
        new_address_change = view.findViewById(R.id.new_address_change);
        item_count_checkout = (TextView) view.findViewById(R.id.item_count_checkout);
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        addToBuyLayout = (RelativeLayout) view.findViewById(R.id.add_to_buy_layout);
        addToBuyrecview = (RecyclerView) view.findViewById(R.id.add_pdt_recycle);
        gift_wrap = (RelativeLayout) view.findViewById(R.id.gift_wrap);
        location = (TextView) view.findViewById(R.id.location);
        payusername =(EditText) view.findViewById(R.id.payusername);
        state = (Spinner) view.findViewById(R.id.statedropdown);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String[] items = new String[]{"Thiruvananthapuram" ,
                "Kollam" ,
                "Pathanamthitta" ,
                "Alappuzha" ,
                "Kottayam" ,
                "Idukki" ,
                "Ernakulam" ,
                "Thrissur" ,
                "Palakkad" ,
                "Malappuram" ,
                "Kozhikkodu" ,
                "Wayanad" ,
                "Kannur" ,
                "Kasaragod"};

        ArrayAdapter<String> adaptera = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, items);

        state.setAdapter(adaptera);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                location.setText((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gift_wrap_label = (TextView) view.findViewById(R.id.gift_wrap_label);
        checkout_add_pdt = (Button) view.findViewById(R.id.checkout_add_pdt);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checkoutProductsAdapter checkadapter = new checkoutProductsAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query)
                FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("temppurchase"), checkoutProduct.class).build());
        checkoutproductsadapter = checkadapter;
        recyclerView.setAdapter(checkadapter);
        checkoutproductsadapter.startListening();
        checkoutproductsadapter.notifyDataSetChanged();


        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_address = new_address.getText().toString();
                String txt_location = location.getText().toString();
                String txt_uname= payusername.getText().toString();

                if (TextUtils.isEmpty(txt_address)||TextUtils.isEmpty(txt_location)||TextUtils.isEmpty(txt_uname)){
                    Toast.makeText(view.getContext() , "Please fill the above fields fields" , Toast.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("address", txt_address );
                    bundle.putString("location", txt_location );
                    bundle.putString("uname", txt_uname );
                    selectorFragment = new payment();
                    selectorFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.viewpager, selectorFragment ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }

            }
        });

        change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_to_new_address.setVisibility(View.VISIBLE);
                blurLayout.startBlur();
            }
        });
        change_to_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_to_new_address.setVisibility(View.GONE);

            }
        });
        new_address_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_new.setText(new_address.getText());
                change_to_new_address.setVisibility(View.GONE);

            }
        });
        checkout_add_pdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBuyLayout.setVisibility(View.VISIBLE);
                addToBuyrecview.setLayoutManager(new LinearLayoutManager(getContext()));
                cartAddMoreAdapter cartadapter = new cartAddMoreAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference("Cart")
                                .child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("products"), cart.class).build());
                adapter = cartadapter;
                addToBuyrecview.setAdapter(cartadapter);
                adapter.startListening();
                adapter.startListening();
                adapter.notifyDataSetChanged();
            }
        });
        addToBuyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBuyLayout.setVisibility(View.GONE);
            }
        });

        if (Globalvariable.flag==true){
            gift_wrap.setVisibility(View.VISIBLE);
            checkout_add_pdt.setVisibility(View.GONE);
        }
        gift_wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check==true){
                    check=false;
                }else {
                    check = true;
                }
                if (check==false){
                    Globalvariable.totalprice = Globalvariable.totalprice+30;
                    totalPrice.setText("Rs. "+String.valueOf(Globalvariable.totalprice+"/-"));
                    gift_wrap_label.setText("Remove Gift Wrap ");
                }else {
                    Globalvariable.totalprice = Globalvariable.totalprice-30;
                    totalPrice.setText("Rs. "+String.valueOf(Globalvariable.totalprice+"/-"));
                    gift_wrap_label.setText("Add Gift Wrap (For Rs. 30)");
                }
            }
        });
        itemcount();
        return view;
    }

    private void itemcount() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference DbRef = FirebaseDatabase.getInstance()
                .getReference("Cart").child("Users").child(auth.getCurrentUser()
                        .getUid()).child("cart").child("temppurchase");
        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long Count = snapshot.getChildrenCount();
                item_count_checkout.setText(String.valueOf(Count)+" product");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}