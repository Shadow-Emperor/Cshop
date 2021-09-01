package com.abhi.cshop.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.abhi.cshop.Adapter.wishlistAdapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.cart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Wishlist extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private wishlistAdapter adapter;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    public Wishlist() {
        // Required empty public constructor
    }

    public static Wishlist newInstance(String param1 , String param2) {
        Wishlist fragment = new Wishlist();
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
        View view = inflater.inflate(R.layout.fragment_wishlist , container , false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_wishlist);
        GridLayoutManager lm = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(lm);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        wishlistAdapter wishadapter = new wishlistAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query) FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(auth.getCurrentUser().getUid()).child("wishlist").child("products"), cart.class).build());
        adapter = wishadapter;
        recyclerView.setAdapter(wishadapter);
        adapter.startListening();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

}