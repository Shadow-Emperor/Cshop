package com.abhi.cshop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.abhi.cshop.Adapter.comment_adapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.comment_model;
import com.abhi.cshop.model.user;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class comment_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rec_cmt;
    private comment_adapter Comment_Adapter;
    private LinearLayoutManager linearlayout;
    private String mParam1;
    private String mParam2;
    private String pdtid;
    private EditText comment_text;
    private Button send;
    LinearLayout comment_head;
    DatabaseReference databaserefrence;
    boolean flag;
    public comment_fragment() {
    }


    public static comment_fragment newInstance(String param1 , String param2 ) {
        comment_fragment fragment = new comment_fragment();
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
        View view = inflater.inflate(R.layout.fragment_comment_fragment , container , false);
        Bundle bundle = this.getArguments();
        pdtid = bundle.getString("pdtid");

        comment_text = (EditText)view.findViewById(R.id.comment_text);
        send = (Button)view.findViewById(R.id.send_comment);
        comment_head = (LinearLayout)view.findViewById(R.id.header_comment);
        rec_cmt = (RecyclerView)view.findViewById(R.id.comment_recycleview);
        linearlayout = new LinearLayoutManager(getActivity());
        rec_cmt.setLayoutManager(linearlayout);
        comment_adapter c_adapter = new comment_adapter(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                FirebaseDatabase.getInstance().getReference().child("Product Details").child("comment").child(pdtid)
                .orderByKey(),comment_model.class).build());
        Comment_Adapter = c_adapter;
        rec_cmt.setAdapter(c_adapter);
        Comment_Adapter.startListening();
        Comment_Adapter.notifyDataSetChanged();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        comment_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_bottom,R.anim.exit_top,R.anim.enter_top,R.anim.exit_bottom);
                transaction.remove(comment_fragment.this);
                transaction.commit();

                //getFragmentManager().beginTransaction().remove(comment_fragment.this)
                        //.setCustomAnimations(R.anim.enter_top,R.anim.exit_bottom,R.anim.enter_bottom,R.anim.exit_top).commit();
            }
        });
        return view;
    }
    private void send(){
        String comment = comment_text.getText().toString();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaserefrence = FirebaseDatabase.getInstance().getReference().child("Users").child(String.valueOf(user));
        String Date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        databaserefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                com.abhi.cshop.model.user Username = snapshot.getValue(user.class);
                String uname = Username.getUsername();
                long currentTime = System.currentTimeMillis();

                DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference().child("Product Details");
                HashMap<String, Object> commentmap = new HashMap<>();
                commentmap.put("comment" , comment);
                commentmap.put("date" , Date);
                commentmap.put("username" , uname);
                commentmap.put("timeinmls",String.valueOf(currentTime));
                DbRef.child("comment").child(pdtid).child(String.valueOf(currentTime)).updateChildren(commentmap);
                comment_text.setText(null);
                Comment_Adapter.notifyDataSetChanged();
                System.out.println("id"+user+"\ndate"+Date+"\ncomment"+comment+"\nusername"+uname+"\n");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}