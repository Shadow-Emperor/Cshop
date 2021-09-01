package com.abhi.cshop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.abhi.cshop.Adapter.chatAdapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.chatmodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class contactusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView chatrecview;
    private LinearLayoutManager linearlayout;
    private chatAdapter ChatAdapter;
    private EditText sendinbox;
    private Button chatsend;
    DatabaseReference Dbref;
    private String identification;
    int count = 0;

    public contactusFragment() {
        // Required empty public constructor
    }


    public static contactusFragment newInstance(String param1 , String param2) {
        contactusFragment fragment = new contactusFragment();
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
        View view = inflater.inflate(R.layout.fragment_contactus , container , false);

        sendinbox = (EditText)view.findViewById(R.id.send_contactus);
        chatsend = (Button) view.findViewById(R.id.chat_contactus);
        chatsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Admin").child("Complaints").child("Users").child(userid);
        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                count = (int) snapshot.getChildrenCount();
                chatrecview = (RecyclerView)view.findViewById(R.id.recview_contactus);
                linearlayout = new LinearLayoutManager(getActivity());
                linearlayout.setStackFromEnd(true);
                chatrecview.setLayoutManager(linearlayout);
                chatAdapter chatadapter = new chatAdapter(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Admin").child("Complaints").child("Users")
                                .child(userid).child(userid)
                                .orderByKey(), chatmodel.class).build());
                ChatAdapter = chatadapter;
                chatrecview.setAdapter(chatadapter);
                chatrecview.smoothScrollToPosition(count);
                ChatAdapter.startListening();
                ChatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private  void  send(){
            String Message = sendinbox.getText().toString();
            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaserefrence = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
            databaserefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.child("identification").exists()) {
                            identification = snapshot.child("identification").getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Dbref = FirebaseDatabase.getInstance().getReference("Admin").child("Complaints").child("Users");
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uid" , user);
                    map.put("identification",identification);
                    map.put("Date",currentDate);
                    Dbref.child(user).updateChildren(map);

                    String uploadId = Dbref.push().getKey();
                    HashMap<String, Object> commentmap = new HashMap<>();
                    commentmap.put("Date" , currentDate);
                    commentmap.put("Time" , currentTime);
                    commentmap.put("Message" , Message);
                    commentmap.put("SendBy",user);
                    Dbref.child(user).child(user).child(uploadId).updateChildren(commentmap);
                    sendinbox.setText(null);
                    ChatAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}