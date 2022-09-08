package com.affinitity.niche.ui.videoPlayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

;
import com.affinitity.niche.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyComentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    ArrayList<RetriverReplyComments> list;

    public ReplyComentsAdapter(Context context, ArrayList<RetriverReplyComments> list) {
       this.context = context;
        this.list = list;
       // this.list.addAll(list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).
                inflate(R.layout.reply_chat_box, parent, false);
        ReplyComentsAdapter.ViewHolder holder = new ReplyComentsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReplyComentsAdapter.ViewHolder myHolder = (ReplyComentsAdapter.ViewHolder) holder;
        myHolder.name.setText(list.get(position).getUser());
        myHolder.comments.setText(list.get(position).getComment());
        Glide.with(context).load(list.get(position).getUser_img()).into(myHolder.profilePic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        TextView name, comments;
        CircleImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.commenter_name);
            comments = (TextView) itemView.findViewById(R.id.comments);
            profilePic = (CircleImageView) itemView.findViewById(R.id.commenter_img);

        }
    }
}