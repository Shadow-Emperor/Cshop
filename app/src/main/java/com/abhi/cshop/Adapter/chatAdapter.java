package com.abhi.cshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abhi.cshop.R;
import com.abhi.cshop.model.chatmodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class chatAdapter extends FirebaseRecyclerAdapter<chatmodel, chatAdapter.myviewholder> {
    public chatAdapter(FirebaseRecyclerOptions<chatmodel> options) {
        super(options);
    }


    @Override
    public void onBindViewHolder(final chatAdapter.myviewholder holder, int position, final chatmodel model) {
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        switch (model.getSendBy()){
            case "Admin":
                holder.send_layout.setVisibility(View.VISIBLE);
                holder.replay_layout.setVisibility(View.GONE);
                holder.chat_message_replay.setText(model.getMessage());
                holder.chat_time_replay.setText(model.getTime());
                break;
            default:
                holder.replay_layout.setVisibility(View.VISIBLE);
                holder.send_layout.setVisibility(View.GONE);
                holder.message.setText(model.getMessage());
                holder.time.setText(model.getTime());
                break;
        }
    }
    @Override
    public chatAdapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new chatAdapter.myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_chat, parent, false));
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        private TextView message,chat_message_replay;
        private TextView time,chat_time_replay;
        private TextView date;
        private String userid;
        private LinearLayout replay_layout,send_layout;


        public myviewholder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.chat_message);
            chat_message_replay = (TextView) itemView.findViewById(R.id.chat_message_replay);
            time = (TextView) itemView.findViewById(R.id.chat_time);
            chat_time_replay = (TextView) itemView.findViewById(R.id.chat_time_replay);
            replay_layout = (LinearLayout) itemView.findViewById(R.id.replay_layout);
            send_layout = (LinearLayout) itemView.findViewById(R.id.send_layout);
        }
    }
}
