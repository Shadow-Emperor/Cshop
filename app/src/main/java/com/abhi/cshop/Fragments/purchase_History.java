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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abhi.cshop.Adapter.historyDetailsAdapter;
import com.abhi.cshop.Adapter.purchaseAdapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.cart;
import com.abhi.cshop.model.history;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class purchase_History extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private purchaseAdapter adaptera;
    private historyDetailsAdapter detailsAdapter;
    public static RelativeLayout disp;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewa;
    private ImageView history_close;
    public static RecyclerView recycler_view_history_details;

    public purchase_History() {
    }


    public static purchase_History newInstance(String param1 , String param2) {
        purchase_History fragment = new purchase_History();
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
        View view =  inflater.inflate(R.layout.fragment_purchase__history , container , false);

        disp = (RelativeLayout)view.findViewById(R.id.disp);
        history_close = (ImageView)view.findViewById(R.id.history_close);

        recyclerViewa = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        recycler_view_history_details = (RecyclerView) view.findViewById(R.id.recycler_view_history_details);

        LinearLayoutManager lm =new LinearLayoutManager(getContext());
        lm.setStackFromEnd(true);
        lm.setReverseLayout(true);
        recyclerViewa.setLayoutManager(lm);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        purchaseAdapter purchaseadapter = new purchaseAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query) FirebaseDatabase.getInstance()
                .getReference("Users").child(auth.getCurrentUser().getUid()).child("purchasehistory"), history.class).build());
        adaptera = purchaseadapter;
        recyclerViewa.setAdapter(purchaseadapter);
        adaptera.startListening();
        adaptera.notifyDataSetChanged();
        history_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp.setVisibility(View.GONE);

            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adaptera != null) {
            adaptera.startListening();
            adaptera.notifyDataSetChanged();
        }
    }
}