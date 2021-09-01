package com.abhi.cshop.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.abhi.cshop.Adapter.cartAdapter;
import com.abhi.cshop.Checkout;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.model.cart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    cartAdapter adapter;
    FirebaseAuth auth;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    public FloatingActionButton submitcart;

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        submitcart = (FloatingActionButton) view.findViewById(R.id.submit_cart);

        TextView cartLabel = view.findViewById(R.id.cart_label);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        cartAdapter cartadapter = new cartAdapter(new FirebaseRecyclerOptions.Builder().setQuery((Query) FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(auth.getCurrentUser().getUid()).child("cart").child("products"), cart.class).build());
        adapter = cartadapter;
        recyclerView.setAdapter(cartadapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 4) {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                builder = new AlertDialog.Builder(CartFragment.this.getActivity());
                builder.setTitle("CONFIRM TO DELETE");
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CartFragment.this.adapter.deleteItem(viewHolder.getAdapterPosition());
                    }
                });
                CartFragment.this.builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                CartFragment cartFragment = CartFragment.this;
                cartFragment.dialog = cartFragment.builder.create();
                CartFragment.this.dialog.show();
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == 1) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addBackgroundColor(ContextCompat.getColor(CartFragment.this.getActivity(), R.color.purple_200))
                            .addActionIcon(R.drawable.ic_delete).addSwipeLeftLabel("Delete").create().decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    return;
                }
            }
        }).attachToRecyclerView(this.recyclerView);

        submitcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) submitcart.getDrawable()).start();
                Intent intent = new Intent(v.getContext(), Checkout.class);
                v.getContext().startActivity(intent);
                Globalvariable.totalprice =0;
            }
        });
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