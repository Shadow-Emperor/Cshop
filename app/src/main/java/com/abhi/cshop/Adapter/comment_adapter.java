package com.abhi.cshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.R;
import com.abhi.cshop.model.comment_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class comment_adapter extends FirebaseRecyclerAdapter<comment_model, comment_adapter.myviewholder> {
    public comment_adapter(FirebaseRecyclerOptions<comment_model> options) {
        super(options);
    }


    @Override
    public void onBindViewHolder(final comment_adapter.myviewholder holder, int position, final comment_model comment) {
        holder.username.setText(comment.getUsername());
        holder.comment.setText(comment.getComment());
        holder.date.setText(comment.getDate());
        //Picasso.get().load(R.drawable.ic_person).into(holder.userimg);
        holder.userimg.setImageResource(R.drawable.ic_person);

    }
    @Override
    public comment_adapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new comment_adapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review, parent, false));
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        private ImageView userimg;
        private TextView username;
        private TextView comment;
        private TextView date;


        public myviewholder(View itemView) {
            super(itemView);

            userimg = (ImageView) itemView.findViewById(R.id.review_user_img);
            username = (TextView) itemView.findViewById(R.id.review_username);
            comment = (TextView) itemView.findViewById(R.id.review_content);
            date = (TextView) itemView.findViewById(R.id.review_date);


        }
    }
}