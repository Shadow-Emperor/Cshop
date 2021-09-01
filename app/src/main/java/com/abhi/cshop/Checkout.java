package com.abhi.cshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.abhi.cshop.Fragments.checkout_delivery;
import com.abhi.cshop.model.Globalvariable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Checkout extends AppCompatActivity {

    public Fragment selectorFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        selectorFragment = new checkout_delivery();
      getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, selectorFragment).commit();

    }

    @Override
    public void onBackPressed() {
        Globalvariable.totalprice = 0;
        Globalvariable.flag=false;
        Globalvariable.count.removeAll(Globalvariable.count);
        Globalvariable.pdtlist.removeAll(Globalvariable.pdtlist);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Cart")
                .child("Users").child(user).child("cart").child("temppurchase").removeValue();
        finish();
    }
}