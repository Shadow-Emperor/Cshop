package com.abhi.cshop.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.LoginSignup;
import com.abhi.cshop.MainActivity;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.user;
import com.abhi.cshop.phonelogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView username,email;
    private TextView editdetailsusername,editaddress;
    private Button logOut,editprofile,editcomplete;
    private CircleImageView profileimg;
    private DatabaseReference DbRef;
    public  Fragment selector;
    LinearLayout linear_phone,linear_email;
    RelativeLayout edildetailslayout;
    private Button history,wishlist;
    public ImageView editbackbtn,profile_photo;
    ProgressDialog pd;
    FirebaseAuth mAuth;
    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logOut = (Button) view.findViewById(R.id.logout);
        profileimg = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.profile_username);
        editdetailsusername = (EditText) view.findViewById(R.id.edit_details_username);
        editaddress = (EditText) view.findViewById(R.id.edit_address);
        email = view.findViewById(R.id.profile_emil);
        history = (Button) view.findViewById(R.id.purchase_history);
        wishlist = (Button) view.findViewById(R.id.profile_wishlist);
        editprofile = (Button) view.findViewById(R.id.edit_profile);
        edildetailslayout = (RelativeLayout) view.findViewById(R.id.edil_details_layout);
        editcomplete = (Button) view.findViewById(R.id.edit_complete);
        editbackbtn = (ImageView) view.findViewById(R.id.edit_back_btn);
        profile_photo = (ImageView) view.findViewById(R.id.profile_photo);
        pd = new ProgressDialog(getContext());
        DbRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector = new Wishlist();
                getFragmentManager().beginTransaction().replace(R.id.framelayout_profile, selector).commit();

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector = new purchase_History();
                getFragmentManager().beginTransaction().replace(R.id.framelayout_profile, selector).commit();

            }
        });
        if (selector==null){
            getFragmentManager().beginTransaction().replace(R.id.framelayout_profile, new Wishlist()).commit();
        }

        getdata();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edildetailslayout.setVisibility(View.VISIBLE);
                edit();
            }
        });
        editcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });
        editbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edildetailslayout.setVisibility(View.GONE);
            }
        });
        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragInfo = new profilepicuploadfragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container , fragInfo).commit();

            }
        });
        return view;
    }


    private void edit() {
        DbRef.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("Username").exists()) {
                        editdetailsusername.setText(snapshot.child("Username").getValue().toString());
                    }
                    if (snapshot.child("Address").exists()) {
                        editaddress.setText(snapshot.child("Address").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getdata() {
        DbRef.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("identification").exists()) {

                    email.setText(snapshot.child("identification").getValue().toString());
                }

                if (snapshot.child("Username").exists()) {
                    username.setText(snapshot.child("Username").getValue().toString());
                } else {
                    edildetailslayout.setVisibility(View.VISIBLE);
                } profileimg.setImageResource(R.drawable.ic_person);
                if (snapshot.child("ProfilPic").exists()){
                    String image = snapshot.child("ProfilPic").getValue().toString();

                    Picasso.get().load(Uri.parse(snapshot.child("ProfilPic").child("imageURL").getValue().toString()))
                            .transform(new RoundedCornersTransformation(50,50))
                            .into(profileimg);
                }else {
                    profileimg.setImageResource(R.drawable.ic_person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void complete() {

        pd.setMessage("Please Wait!");
        pd.show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String txt_username = editdetailsusername.getText().toString();
        String txt_address = editaddress.getText().toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("Username", txt_username);
        map.put("Address", txt_address);
        map.put("id", mAuth.getCurrentUser().getUid());
        System.out.println(map+"\n"+mAuth.getCurrentUser().getPhoneNumber());
        DbRef.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(getView().getContext(), "Success", Toast.LENGTH_SHORT).show();
                    edildetailslayout.setVisibility(View.GONE);
                }
            }
        });

    }

    private void logout() {
        Globalvariable.selectorFragmentback=null;
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginSignup.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        getActivity().finish();
    }
}