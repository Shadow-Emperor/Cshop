package com.abhi.cshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.abhi.cshop.R;
import com.abhi.cshop.model.modelimages;
import com.abhi.cshop.product_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class product_images_adapter extends FirebaseRecyclerAdapter<modelimages, product_images_adapter.myviewholder> {
        private Context mContext;

        public product_images_adapter(FirebaseRecyclerOptions<modelimages> options,Context context) {
                super(options);
                this.mContext = context;
        }

        @Override
        public void onBindViewHolder(final product_images_adapter.myviewholder holder, int position, final modelimages model) {
        System.out.println(model.getImageURL());
        Picasso.get().load(Uri.parse(model.getImageURL())).into(holder.pdtlistimg);
                holder.imagecard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                               Uri zoom =Uri.parse(model.getImageURL());
                               if (mContext instanceof product_main){
                                       ((product_main)mContext).zoom(zoom);
                               }

                        }
                });

        }
        @Override
        public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new myviewholder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_card_view, parent, false));
        }

        public class myviewholder extends RecyclerView.ViewHolder {
                ImageView pdtlistimg,zoomimage;
                RelativeLayout imagezoom;
                CardView imagecard;



                public myviewholder(View itemView) {
                        super(itemView);

                        pdtlistimg = (ImageView) itemView.findViewById(R.id.product_images);
                        imagecard = (CardView) itemView.findViewById(R.id.card_view);

                }
        }

}