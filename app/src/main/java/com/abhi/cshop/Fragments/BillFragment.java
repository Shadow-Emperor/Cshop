package com.abhi.cshop.Fragments;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhi.cshop.Checkout;
import com.abhi.cshop.R;
import com.abhi.cshop.model.Globalvariable;
import com.abhi.cshop.product_main;


public class BillFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView price;
    private ImageView confirm;
    private RelativeLayout bill;
    public BillFragment() {
        // Required empty public constructor
    }


    public static BillFragment newInstance(String param1 , String param2) {
        BillFragment fragment = new BillFragment();
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
        String total = getArguments().getString("total");
        View view = inflater.inflate(R.layout.fragment_bill , container , false);
        price = view.findViewById(R.id.total);
        confirm = view.findViewById(R.id.confirm);
        bill = (RelativeLayout) view.findViewById(R.id.bill);
        price.setText("You were charged "+total+ "Rs.");
        ((Animatable) confirm.getDrawable()).start();
        Globalvariable.pdtlist.removeAll(Globalvariable.pdtlist);
        Globalvariable.count.removeAll(Globalvariable.count);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), product_main.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}