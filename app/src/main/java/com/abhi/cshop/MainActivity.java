package com.abhi.cshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.Fragments.CartFragment;
import com.abhi.cshop.Fragments.HomeFragment;
import com.abhi.cshop.Fragments.ProfileFragment;
import com.abhi.cshop.Fragments.SearchFragment;
import com.abhi.cshop.Fragments.Wishlist;
import com.abhi.cshop.Fragments.contactusFragment;
import com.abhi.cshop.Fragments.inboxFragment;
import com.abhi.cshop.Fragments.purchase_History;
import com.abhi.cshop.model.Globalvariable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public Fragment selectorFragment, selector;
    private TextView logout, username, email, wishlist, history, contactus, terms,appversion;
    DatabaseReference DbRef;
    FirebaseAuth mAuth;
    boolean flag;
    ImageView user_image;
    RelativeLayout option;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        selectorFragment = Globalvariable.selectorFragmentback;
        logout = findViewById(R.id.logout_option);
        username = findViewById(R.id.user_username);
        email = findViewById(R.id.user_emil);
        wishlist = findViewById(R.id.draw_wishlist);
        history = findViewById(R.id.draw_history);
        contactus = findViewById(R.id.draw_contactus);
        terms = findViewById(R.id.draw_terms);
        user_image = findViewById(R.id.user_image);
        appversion = findViewById(R.id.appversion);
        DbRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String versionName = BuildConfig.VERSION_NAME;
        appversion.setText("version "+versionName);
        load();

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        option = findViewById(R.id.drawer);
        flag = false;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        Globalvariable.selectorFragmentback = new ProfileFragment();
                        Globalvariable.fragmentid = "profile";
                        break;

                    case R.id.nav_cart:
                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("Cart")
                                .child("Users").child(user).child("cart").child("temppurchase").removeValue();
                        selectorFragment = new CartFragment();
                        Globalvariable.selectorFragmentback = new CartFragment();
                        Globalvariable.flag = false;
                        Globalvariable.count.removeAll(Globalvariable.count);
                        Globalvariable.pdtlist.removeAll(Globalvariable.pdtlist);
                        Globalvariable.fragmentid = "cart";
                        break;

                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        Globalvariable.selectorFragmentback = new HomeFragment();
                        Globalvariable.fragmentid = "home";
                        break;

                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        Globalvariable.selectorFragmentback = new SearchFragment();
                        Globalvariable.fragmentid = "search";
                        break;

                    case R.id.nav_menu:
                        if (flag == false) {
                            opendraw();
                        } else {
                            close();
                        }
                        break;
                }
                if (selectorFragment == null) {
                    return true;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                return true;
            }
        });
        if (selectorFragment == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        } else {
            switch (Globalvariable.fragmentid) {
                case "profile":
                    bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                    break;
                case "cart":
                    Globalvariable.flag = false;
                    FirebaseDatabase.getInstance().getReference("Cart")
                            .child("Users").child(user).child("cart").child("temppurchase").removeValue();
                    bottomNavigationView.setSelectedItemId(R.id.nav_cart);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                    break;
                case "home":
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                    break;
                case "search":
                    bottomNavigationView.setSelectedItemId(R.id.nav_search);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                    break;
                      /* case "menu":
                           bottomNavigationView.setSelectedItemId(R.id.nav_menu);
                           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                           break;*/

            }

        }
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Globalvariable.selectorFragmentback = null;
                startActivity(new Intent(MainActivity.this , LoginSignup.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                MainActivity.this.finish();
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector = new Wishlist();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selector).commit();
                close();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector = new purchase_History();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selector).commit();
                close();
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector = new contactusFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selector).commit();
                close();
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void close() {
        Transition transition = new Slide(Gravity.RIGHT);
        transition.setDuration(400);
        transition.addTarget(option);
        TransitionManager.beginDelayedTransition(option , transition);
        option.setVisibility(View.GONE);
        flag = false;
    }
    private void opendraw() {
        Transition transition = new Slide(Gravity.RIGHT);
        transition.setDuration(600);
        transition.addTarget(option);
        TransitionManager.beginDelayedTransition(option,transition);
        option.setVisibility(View.VISIBLE);
        flag = true;
    }
    private void load() {
        DbRef.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("identification").exists()) {

                    email.setText(snapshot.child("identification").getValue().toString());
                }

                if (snapshot.child("Username").exists()) {
                    username.setText(snapshot.child("Username").getValue().toString());
                }
                if (snapshot.child("ProfilPic").exists()){
                    String image = snapshot.child("ProfilPic").getValue().toString();

                    Picasso.get().load(Uri.parse(snapshot.child("ProfilPic").child("imageURL").getValue().toString()))
                            .transform(new RoundedCornersTransformation(50,50))
                            .into(user_image);
                }else {
                    user_image.setImageResource(R.drawable.ic_person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}