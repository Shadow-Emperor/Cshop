package com.abhi.cshop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhi.cshop.Adapter.cartAdapter;
import com.abhi.cshop.Adapter.chatAdapter;
import com.abhi.cshop.Adapter.comment_adapter;
import com.abhi.cshop.Adapter.inboxAdapter;
import com.abhi.cshop.Adapter.purchaseAdapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.cart;
import com.abhi.cshop.model.chatmodel;
import com.abhi.cshop.model.comment_model;
import com.abhi.cshop.model.history;
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


public class inboxFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView inbox_recview,chat_recview;
    private String mParam1;
    private String mParam2;
    private inboxAdapter adapter;
    private chatAdapter ChatAdapter;
    public static RelativeLayout inbox_chat;
    public static LinearLayout admin_contact;
    private LinearLayoutManager linearlayout;
    private EditText send_inbox;
    private Button chat_send;
    DatabaseReference Dbref;
    private String identification;
    int count = 0;

    public inboxFragment() {
        // Required empty public constructor
    }

    public static inboxFragment newInstance(String param1 , String param2) {
        inboxFragment fragment = new inboxFragment();
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
        View view = inflater.inflate(R.layout.fragment_inbox , container , false);
        inbox_chat = (RelativeLayout)view.findViewById(R.id.inbox_chat);

        admin_contact =(LinearLayout)view.findViewById(R.id.admin_contact);
        inbox_recview = (RecyclerView)view.findViewById(R.id.inbox_recview);
        LinearLayoutManager lm =new LinearLayoutManager(getContext());
        lm.setStackFromEnd(true);
        lm.setReverseLayout(true);
        inbox_recview.setLayoutManager(lm);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        inboxAdapter inboxadapter = new inboxAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query) FirebaseDatabase.getInstance()
                .getReference("Users").child(auth.getCurrentUser().getUid()).child("purchasehistory"), history.class).build());
        adapter = inboxadapter;
        inbox_recview.setAdapter(inboxadapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

        send_inbox = (EditText)view.findViewById(R.id.send_inbox);
        chat_send = (Button) view.findViewById(R.id.chat_send);
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        admin_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inbox_chat.setVisibility(View.VISIBLE);
            }
        });
        inbox_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inbox_chat.setVisibility(View.GONE);
            }
        });
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Admin").child("Complaints").child("Users").child(userid);
        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                count = (int) snapshot.getChildrenCount();
                chat_recview = (RecyclerView)view.findViewById(R.id.chat_recview);
                linearlayout = new LinearLayoutManager(getActivity());
                linearlayout.setStackFromEnd(true);
                chat_recview.setLayoutManager(linearlayout);
                chatAdapter chatadapter = new chatAdapter(new  FirebaseRecyclerOptions.Builder().setQuery((Query)
                        FirebaseDatabase.getInstance().getReference().child("Admin").child("Complaints").child("Users")
                                .child(userid).child(userid)
                                .orderByKey(), chatmodel.class).build());
                ChatAdapter = chatadapter;
                chat_recview.setAdapter(chatadapter);
                chat_recview.smoothScrollToPosition(count);
                ChatAdapter.startListening();
                ChatAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void send() {
        String Message = send_inbox.getText().toString();
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
                send_inbox.setText(null);
                ChatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
            adapter.notifyDataSetChanged();
        }
    }
}