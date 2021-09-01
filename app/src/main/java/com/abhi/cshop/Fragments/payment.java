 package com.abhi.cshop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.Adapter.creditcardfourdigitformat;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.cart;
import com.abhi.cshop.model.model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class payment extends Fragment {

    private EditText creditcardnumber,CardholderName,expdate,CvvCvc;
    private TextView price_pay;
    private Button payment;
    private String address,location,uname;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth_user;
    private String mParam1;
    private String mParam2;
    public Fragment selectorFragment;
    private DatabaseReference databaseref,Dbrefa;
    public payment() {
    }

   
    public static payment newInstance(String param1 , String param2) {
        payment fragment = new payment();
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
        View view = inflater.inflate(R.layout.fragment_payment , container , false);
        Bundle bundle = this.getArguments();

        address = bundle.getString("address");
        location = bundle.getString("location");
        uname = bundle.getString("uname");

        creditcardnumber = (EditText) view.findViewById(R.id.Credit_card_number);
        CardholderName = (EditText) view.findViewById(R.id.cardholder_name);
        CvvCvc = (EditText) view.findViewById(R.id.cvv_cvc);

        price_pay = (TextView) view.findViewById(R.id.price_pay);
        expdate = (EditText) view.findViewById(R.id.exp_date);
        creditcardnumber.addTextChangedListener(new creditcardfourdigitformat());
        payment =(Button)  view.findViewById(R.id.payment);
        auth_user = FirebaseAuth.getInstance();
        String user = auth_user.getCurrentUser().getUid();

        price_pay.setText("Rs. "+String.valueOf(Globalvariable.totalprice)+"/-");
        databaseref = FirebaseDatabase.getInstance().getReference("Users").child(user);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence foo = creditcardnumber.getText();
                String Name = String.valueOf(CardholderName.getText());
                String Cvv = String.valueOf(CvvCvc.getText());
                String Date = String.valueOf(expdate.getText());

                String bar = foo.toString();
                if (bar.length()==0||bar.length()<=18||TextUtils.isEmpty(Name)||TextUtils.isEmpty(Cvv)||TextUtils.isEmpty(Date)) {
                    Toast.makeText(v.getContext() , "please fill all fields" , Toast.LENGTH_SHORT).show();
                } else {
                    buy_product(bar,Name);
                }
            }
        });

        return view;
    }

    private void buy_product(String bar,String Name) {
        DatabaseReference DatabRef = FirebaseDatabase.getInstance()
                .getReference("Cart").child("Users").child(auth_user.getCurrentUser().getUid()).child("cart").child("temppurchase");
        DatabRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long Count ;

                if (snapshot.exists()){
                    Count = snapshot.getChildrenCount();
                    String first4digits = bar.substring(0 , 4);
                    String second4digits = bar.substring(5 , 9);
                    String third4digits = bar.substring(10 , 14);
                    String last4digits = bar.substring(15 , 19);

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat date = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
                    String formattedDate = date.format(c);
                    String uploadId = databaseref.push().getKey();

                    HashMap<String, Object> buymap = new HashMap<>();
                    buymap.put("CreditCardNo",first4digits+second4digits+third4digits+last4digits);
                    buymap.put("Date",formattedDate);
                    buymap.put("Card Holder Name",Name);
                    buymap.put("NoofProducts",Count);
                    buymap.put("Price", Globalvariable.totalprice);
                    buymap.put("PurchaseId",uploadId);
                    databaseref.child("purchasehistory").child(uploadId).updateChildren(buymap);

                    Dbrefa = FirebaseDatabase.getInstance().getReference("Admin").child("Orders");
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    HashMap<String, Object> addmap = new HashMap<>();
                    addmap.put("CreditCardNo",first4digits+second4digits+third4digits+last4digits);
                    addmap.put("Date",formattedDate);
                    addmap.put("Card Holder Name",Name);
                    addmap.put("NoofProducts",Count);
                    addmap.put("Price", Globalvariable.totalprice);
                    addmap.put("PurchaseId",uploadId);
                    addmap.put("Address",address);
                    addmap.put("Location",location);
                    addmap.put("UserName",uname);
                    addmap.put("flag",0);
                    Dbrefa.child("orderlist").child(uploadId).updateChildren(addmap);

                    DatabaseReference dbref = FirebaseDatabase.getInstance()
                            .getReference("Cart").child("Users").child(auth_user.getCurrentUser().getUid()).child("cart");
                    dbref.child("temppurchase").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long temp = snapshot.getChildrenCount();
                            for (int i=0;i<temp;i++){
                                String pdtid = Globalvariable.pdtlist.get(i);
                                Long count = Globalvariable.count.get(i);

                                System.out.println(Globalvariable.count.get(i)+"\t"+Globalvariable.pdtlist.get(i));
                                DatabaseReference Dbref = FirebaseDatabase.getInstance()
                                        .getReference("Product Details").child("Products");
                                Dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            if (snapshot.hasChild(pdtid)) {
                                                long maxpdt = (long) snapshot.child(pdtid).child("stock").getValue();
                                                long popularity = (long) snapshot.child(pdtid).child("Popularity").getValue();
                                                long pdtcount = maxpdt-count;
                                                long Popularity = popularity+1;
                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("stock" , pdtcount);
                                                map.put("Popularity" , Popularity);
                                                Dbref.child(pdtid).updateChildren(map);

                                                String image = String.valueOf(snapshot.child(pdtid).child("imageURL").getValue());
                                                String pdtname = String.valueOf(snapshot.child(pdtid).child("productName").getValue());
                                                long pdtprice = (long) snapshot.child(pdtid).child("price").getValue();
                                                HashMap<String, Object> productmap = new HashMap<>();
                                                productmap.put("image",image);
                                                productmap.put("name",pdtname);
                                                productmap.put("price",pdtprice);
                                                productmap.put("count",count);
                                                databaseref.child("purchasehistory").child(uploadId).child("purchaseproducts")
                                                        .child(pdtid).setValue(productmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Dbrefa.child("orderlist").child(uploadId).child("purchaseproducts").child(pdtid).setValue(productmap);
                                                        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                                                                .getReference("Cart").child("Users").child(auth_user.getCurrentUser().getUid()).child("cart").child("products");
                                                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()){
                                                                    if (snapshot.hasChild(pdtid)) {
                                                                        long update = (long) snapshot.child(pdtid).child("stock").getValue();
                                                                        long init = update-count;
                                                                        HashMap<String, Object> cartmap = new HashMap<>();
                                                                        cartmap.put("stock" , init);
                                                                        dbRef.child(pdtid).updateChildren(cartmap);


                                                                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if (snapshot.exists()) {
                                                                                    long temp = (long) snapshot.child(pdtid).child("stock").getValue();
                                                                                    if (temp<=0) {
                                                                                        dbRef.child(pdtid).removeValue();
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("total", String.valueOf(Globalvariable.totalprice));
                                                        selectorFragment = new BillFragment();
                                                        selectorFragment.setArguments(bundle);
                                                        getFragmentManager().beginTransaction().replace(R.id.viewpager, selectorFragment).commit();

                                                        dbref.child("temppurchase").removeValue();
                                                        creditcardnumber.setText(null);
                                                        Globalvariable.pdtlist.removeAll(Globalvariable.pdtlist);
                                                        Globalvariable.count.removeAll(Globalvariable.count);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                            }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}