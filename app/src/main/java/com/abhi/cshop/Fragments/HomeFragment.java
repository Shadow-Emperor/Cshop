package com.abhi.cshop.Fragments;

import android.graphics.drawable.Animatable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.cshop.Adapter.MyCategoriesExpandableListAdapter;
import com.abhi.cshop.Adapter.listAdapter;
import com.abhi.cshop.Adapter.myadapter;
import com.abhi.cshop.R;
import com.abhi.cshop.model.ConstantManager;
import com.abhi.cshop.model.DataItem;
import com.abhi.cshop.model.SubCategoryItem;
import com.abhi.cshop.model.Utils;
import com.abhi.cshop.model.model;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public myadapter adapter;
    public listAdapter listadapter;
    private String mParam1;
    private String mParam2;
    public RecyclerView recview, recyclview;
    private LinearLayoutManager lm;
    private LinearLayout filter_layout;
    private Button Relevance,Popularity, Price_L_H,Pric_H_L,Newest,Reset,filter;
    private FloatingActionButton filter_submit;
    private TextView filter_label;
    private ImageSlider mainslider;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    private DatabaseReference childref=databaseReference.child("URL");
    private static String filter_id;
    private View view;
    private  Fragment selector;
    private boolean flag=false,count=true;
    long fil_value_end,fil_value_begin;
    String parent,brand,category;
    private ImageView inbox;
    private ExpandableListView lvCategory;
    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;
    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;

    private ArrayList<String> Price = new ArrayList<String>();
    private ArrayList<String> Brand = new ArrayList<String>();
    private ArrayList<String> Category = new ArrayList<String>();



    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Relevance = (Button)view.findViewById(R.id.sort_relevance);
        Popularity = (Button)view.findViewById(R.id.sort_popularity);
        Price_L_H = (Button)view.findViewById(R.id.sort_price_low_high);
        Pric_H_L = (Button)view.findViewById(R.id.sort_price_high_low);
        Newest = (Button)view.findViewById(R.id.sort_latest);
        Reset = (Button)view.findViewById(R.id.sort_reset);
        filter_label=(TextView)view.findViewById(R.id.filter_label);
        filter = (Button)view.findViewById(R.id.filter_view);
        filter_layout = (LinearLayout)view.findViewById(R.id.filter_layout);
        filter_submit = (FloatingActionButton)view.findViewById(R.id.filter_submit);
        inbox = (ImageView)view.findViewById(R.id.inbox);
        FrameLayout frameloyout = (FrameLayout)view.findViewById(R.id.frameloyout_inbox);
        //filter view

        recview = (RecyclerView) view.findViewById(R.id.recycler_view_filter);
        lm = new GridLayoutManager(getActivity() , 1);
        recview.setLayoutManager(lm);
        myadapter myAdapter = new myadapter(new FirebaseRecyclerOptions.Builder().setQuery((Query)
                FirebaseDatabase.getInstance().getReference().child("Product Details").child("Products").limitToFirst(10) , model.class).build() , getActivity());
        adapter = myAdapter;
        recview.setAdapter(myAdapter);


        //pdt list
        Relevance.setOnClickListener(this);
        Popularity.setOnClickListener(this);
        Price_L_H.setOnClickListener(this);
        Pric_H_L.setOnClickListener(this);
        Newest.setOnClickListener(this);
        Reset.setOnClickListener(this);
        filter.setOnClickListener(this);

        listadapter_view();



        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainslider=(ImageSlider)view.findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("offers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                            remoteimages.add(new SlideModel(data.child("imageURL").getValue().toString(), ScaleTypes.FIT));

                        mainslider.setImageList(remoteimages, ScaleTypes.FIT);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        filter_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 ((Animatable) filter_submit.getDrawable()).start();
                for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++ ){

                    String isChecked = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

                    if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
                    {
                        System.out.println(MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
                    }

                    for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++ ){

                        String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                        if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
                        {

                            System.out.println(
                                    MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " "+(j+1));
                            if(MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME)=="Price") {
                                parent = "Price";
                            }else if (MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME)=="Brand"){
                                parent = "Brand";
                            }else if (MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME)=="Category") {
                                parent = "Category";
                            }else{

                            }
                            switch (j+1){
                                case 1:
                                    fil_value_begin=0;
                                    fil_value_end=5000;
                                    brand= "CANON";
                                    category="Drones";
                                    break;
                                case 2:
                                    fil_value_begin=5001;
                                    fil_value_end=10000;
                                    brand="GOPRO";
                                    category="Point and Shoot";
                                    break;
                                case 3:
                                    fil_value_begin=10001;
                                    fil_value_end=15000;
                                    brand="NIKON";
                                    category="IP Cameras";
                                    break;
                                case 4:
                                    fil_value_begin=15001;
                                    fil_value_end=20000;
                                    brand="SONY";
                                    category="Sports & Action";
                                    break;
                                case 5:
                                    fil_value_begin=20001;
                                    fil_value_end=30000;
                                    brand="PANASONIC";
                                    category="DSLR & Mirrorless";
                                    break;
                                case 6:
                                    fil_value_begin=30001;
                                    fil_value_end=50000;
                                    brand="FUJIFILM";
                                    category="Camcorders";
                                    break;
                                case 7:
                                    fil_value_begin=500001;
                                    fil_value_end=999999999;
                                    brand="LEICA";
                                    category="Lense";
                                    break;
                                case 8:
                                    brand="OLYMPUS";
                                    break;
                                case 9:
                                    brand="RICOH";
                                    break;
                                case 10:
                                    brand="SAMSUNG";
                                    break;
                                case 11:
                                    brand="SIGMA";
                                    break;
                            }
                            filter_main();
                        }

                    }
                    Transition transition = new Slide(Gravity.LEFT);
                    transition.setDuration(400);
                    transition.addTarget(filter_layout);
                    TransitionManager.beginDelayedTransition(filter_layout,transition);
                    filter_layout.setVisibility(View.INVISIBLE);
                    flag = false;
                }
            }
        });
        setupReferences();

        RelativeLayout rel = view.findViewById(R.id.rframe);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.VISIBLE);
                selector = new inboxFragment();
                getFragmentManager().beginTransaction().replace(R.id.frameloyout_inbox, selector).commit();
            }
        });
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void filter_main() {

        if (parent=="Price") {
           if (count ==true) {
               GridLayoutManager linearfil = new GridLayoutManager(getActivity(),2);
               recyclview.setLayoutManager(linearfil);
               listAdapter listadptr = new listAdapter(new FirebaseRecyclerOptions.Builder()
                       .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                               .child("Product Details").child("Products").orderByChild("price").startAt(fil_value_begin).endAt(fil_value_end) , model.class).build());
               listadapter = listadptr;
               recyclview.setAdapter(listadptr);
               listadapter.startListening();
               listadapter.notifyDataSetChanged();
               adapter.stopListening();
               count =false;
           }
        }else if (parent=="Brand") {
            System.out.println(brand);
            GridLayoutManager linearfil = new GridLayoutManager(getActivity(),2);
            recyclview.setLayoutManager(linearfil);
            listAdapter listadptr = new listAdapter(new FirebaseRecyclerOptions.Builder()
                    .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                            .child("Product Details").child("Products").orderByChild("Brand").equalTo(brand), model.class).build());
            listadapter = listadptr;
            recyclview.setAdapter(listadptr);
            listadapter.startListening();
            adapter.stopListening();

        }else if (parent=="Category") {
            GridLayoutManager linearfil = new GridLayoutManager(getActivity(),2);
            recyclview.setLayoutManager(linearfil);
            listAdapter listadptr = new listAdapter(new FirebaseRecyclerOptions.Builder()
                    .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                            .child("Product Details").child("Products").orderByChild("Brand").equalTo(category) , model.class).build());
            listadapter = listadptr;
            recyclview.setAdapter(listadptr);
            listadapter.startListening();
            listadapter.notifyDataSetChanged();
            adapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.sort_relevance:
                filter_id = "Relevance";
                listadapter_view();
                adapter.stopListening();
                break;
            case R.id.sort_popularity:
                filter_id = "popularity";
                listadapter_view();
                adapter.stopListening();
                break;
            case R.id.sort_price_low_high:
                filter_id = "low_high";
                listadapter_view();
                adapter.stopListening();
                break;
            case R.id.sort_price_high_low:
                filter_id = "high_low";
                listadapter_view();
                adapter.stopListening();
                break;
            case R.id.sort_latest:
                filter_id = "latest";
                listadapter_view();
                adapter.stopListening();
                break;
            case R.id.sort_reset:
                filter_id = "reset";
                filter_label.setText("Top Selling");
                listadapter_view();
                adapter.startListening();
                adapter.notifyDataSetChanged();
                count=true;
                break;
            case R.id.filter_view:
                if(flag ==false) {
                    Transition transition = new Slide(Gravity.LEFT);
                    transition.setDuration(600);
                    transition.addTarget(filter_layout);
                    TransitionManager.beginDelayedTransition(filter_layout,transition);
                    filter_layout.setVisibility(View.VISIBLE);
                    flag = true;
                }else{
                    Transition transition = new Slide(Gravity.LEFT);
                    transition.setDuration(400);
                    transition.addTarget(filter_layout);
                    TransitionManager.beginDelayedTransition(filter_layout,transition);
                    filter_layout.setVisibility(View.INVISIBLE);
                    flag = false;
                }
                break;
        }
    }
    private void listadapter_view() {

        recyclview = (RecyclerView) view.findViewById(R.id.recycler_view_pdtlist);
        if (filter_id!=null) {
            switch (filter_id) {
                case "Relevance":
                    filter_label.setText("Relevance");
                    GridLayoutManager linear0 = new GridLayoutManager(getActivity(),2);
                    recyclview.setLayoutManager(linear0);
                    listAdapter Relevanceadpter = new listAdapter(new FirebaseRecyclerOptions.Builder()
                            .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                                    .child("Product Details").child("Products").orderByChild("price"), model.class).build());
                    listadapter = Relevanceadpter;
                    recyclview.setAdapter(Relevanceadpter);
                    listadapter.startListening();
                    listadapter.notifyDataSetChanged();
                    break;
                case "popularity":
                    filter_label.setText("Popularity");
                    GridLayoutManager linear1 = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,true){
                        @Override
                        public boolean  isLayoutRTL() {
                            return true;
                        }
                    };
                    recyclview.setLayoutManager(linear1);
                    listAdapter popularityadapter = new listAdapter(new FirebaseRecyclerOptions.Builder()
                            .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                                    .child("Product Details").child("Products").orderByChild("Popularity") , model.class).build());
                    listadapter = popularityadapter;
                    recyclview.setAdapter(popularityadapter);
                    listadapter.startListening();
                    listadapter.notifyDataSetChanged();
                    break;
                case "low_high":
                    filter_label.setText("Price Low to High");
                    GridLayoutManager linear2 = new GridLayoutManager(getActivity(),2);
                    recyclview.setLayoutManager(linear2);
                    listAdapter listadptr = new listAdapter(new FirebaseRecyclerOptions.Builder()
                            .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                                    .child("Product Details").child("Products").orderByChild("price"), model.class).build());
                    listadapter = listadptr;
                    recyclview.setAdapter(listadptr);
                    listadapter.startListening();
                    listadapter.notifyDataSetChanged();
                    break;
                case "high_low":
                    filter_label.setText("Price High to Low");
                    GridLayoutManager linear3 = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,true){
                        @Override
                        public boolean  isLayoutRTL() {
                            return true;
                        }
                    };
                    recyclview.setLayoutManager(linear3);
                    listAdapter h_to_ladapter = new listAdapter(new FirebaseRecyclerOptions.Builder()
                            .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                                    .child("Product Details").child("Products").orderByChild("price") , model.class).build());
                    listadapter = h_to_ladapter;
                    recyclview.setAdapter(h_to_ladapter);
                    listadapter.startListening();
                    listadapter.notifyDataSetChanged();
                    break;
                case "latest":
                    filter_label.setText("Latest");
                    GridLayoutManager linear4 = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,true){
                        @Override
                        public boolean  isLayoutRTL() {
                            return true;
                        }
                    };
                    recyclview.setLayoutManager(linear4);
                    listAdapter latestadapter = new listAdapter(new FirebaseRecyclerOptions.Builder()
                            .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                                    .child("Product Details").child("Products") , model.class).build());
                    listadapter = latestadapter;
                    recyclview.setAdapter(latestadapter);
                    listadapter.startListening();
                    listadapter.notifyDataSetChanged();
                    break;

            }
        }else{
            GridLayoutManager linear = new GridLayoutManager(getActivity(),2);
            recyclview.setLayoutManager(linear);
            listAdapter listadptr = new listAdapter(new FirebaseRecyclerOptions.Builder()
                    .setQuery((Query) FirebaseDatabase.getInstance().getReference()
                            .child("Product Details").child("Products"), model.class).build());
            listadapter = listadptr;
            recyclview.setAdapter(listadptr);
            listadapter.startListening();
            listadapter.notifyDataSetChanged();
            adapter.startListening();
            adapter.notifyDataSetChanged();
        }

    }


    private void setupReferences() {

        lvCategory = view.findViewById(R.id.lvCategory);
        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        Price.add("Rs. 5000 and Below");
        Price.add("Rs. 5001 - 10000");
        Price.add("Rs. 10001 - 15000");
        Price.add("Rs. 15001 - 20000");
        Price.add("Rs. 20001 - 30000");
        Price.add("Rs. 30001 - 50000");
        Price.add("Rs. 50001 and above");

        Brand.add("Canon");
        Brand.add("GoPro");
        Brand.add("Nikon");
        Brand.add("Sony");
        Brand.add("Panasonic");
        Brand.add("Fujifilm");
        Brand.add("Leica");
        Brand.add("Olympus");
        Brand.add("Ricoh");
        Brand.add("Samsung");
        Brand.add("Sigma");

        Category.add("Drones");
        Category.add("Point and Shoot");
        Category.add("IP Cameras");
        Category.add("Sports & Action");
        Category.add("DSLR & Mirrorless");
        Category.add("Camcorders");
        Category.add("Lense");

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Price");


        arSubCategory = new ArrayList<>();
        for(int i = 0; i < 7; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(Price.get(i));
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Brand");
        arSubCategory = new ArrayList<>();
        for(int j = 0; j < 11; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(Brand.get(j));
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Category");
        arSubCategory = new ArrayList<>();
        for(int k = 0; k < 7; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(Category.get(k));
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);


        for(DataItem data : arCategory){
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList =new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID,data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME,data.getCategoryName());

            int countIsChecked = 0;
            for(SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID,subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME,subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID,subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED,subCategoryItem.getIsChecked());

                if(subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if(countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            }else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED,data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(getActivity(),parentItems,childItems,false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}