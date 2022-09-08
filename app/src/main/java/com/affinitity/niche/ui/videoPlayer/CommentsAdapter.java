package com.affinitity.niche.ui.videoPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.ConstantsStored;
import com.affinitity.niche.R;
import com.affinitity.niche.RetriverComments;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    Context context;
    DatabaseReference reference;
    String userName,userId,user_img;
    ArrayList<RetriverComments> profiles;
    ArrayList<RetriverReplyComments> lists;
    Firebase reference1, reference2;
    private ReplyComentsAdapter replyComentsAdapter;

    public CommentsAdapter(Context c, ArrayList<RetriverComments> p)
    {
        context = c;
        profiles = p;
        SharedPreferences prefs = context.getSharedPreferences(ConstantsStored.MY_PREFS_NAME, MODE_PRIVATE);
        userName = prefs.getString(ConstantsStored.name, "");
        user_img = prefs.getString(ConstantsStored.user_img, "");
        userId = prefs.getString(ConstantsStored.user_id, "");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        lists = new ArrayList<RetriverReplyComments>();
       // replyComentsAdapter = new ReplyComentsAdapter((Activity) context,lists);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box, null);
        return new CommentsAdapter.MyViewHolder(view);

       // return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_box,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(profiles.get(position).getUser());
        holder.comments.setText(profiles.get(position).getComment());
        holder.like_count.setText(profiles.get(position).getLike());

       // holder.reply_cmt.setText(profiles.get(position).getReplie_coments());
        Glide.with(context).load(profiles.get(position).getUser_img()).into(holder.profilePic);
        if (holder.recyclerView != null) {
            holder.recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.context,
                    LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(replyComentsAdapter);
        }
        holder.onPostExecute(position, lists);

        holder.onClick(position);



      /*  if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,comments,like_count,reply_btn,reply_cmt;
        CircleImageView profilePic;
        ImageView like_btn;
        RecyclerView recyclerView;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.commenter_name);
            like_count = (TextView) itemView.findViewById(R.id.like_count);
            reply_btn = (TextView) itemView.findViewById(R.id.reply_btn);
            comments = (TextView) itemView.findViewById(R.id.comments);
           // reply_cmt = (TextView) itemView.findViewById(R.id.reply_cmt);
            profilePic = (CircleImageView) itemView.findViewById(R.id.commenter_img);
            like_btn = (ImageView) itemView.findViewById(R.id.like_btn);
            //recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_inner_replies);


          /*  CommentsAdapter.MyViewHolder viewHolder = new CommentsAdapter.MyViewHolder(itemView);*/
            recyclerView = (RecyclerView)itemView
                    .findViewById(R.id.rv_inner_replies);

        }
        public void onClick(final int position)
        {
            like_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    like_btn.setBackgroundResource(R.drawable.like_fill);

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("comments").child(profiles.get(position).getId());
                    Map<String, Object> updates = new HashMap<String,Object>();
                  //  updates.put("liked_users", userId);
                   // updates.put("inner_comment", newscore);

                    if (profiles.get(position).getLike().isEmpty()) {

                        updates.put("like","1" );
                    } else {
                        int count = Integer.parseInt(profiles.get(position).getLike());
                        int total = count + 1;
                        updates.put("like",String.valueOf(total) );
                    }


                  //  int total = Integer.parseInt(profiles.get(position).getLike()+1);
                    String [] likedUsers = "1".split(",");
                    if( Arrays.asList(likedUsers).contains(userId) == true ){
                        List<String> list = new ArrayList<String>(Arrays.asList(likedUsers));
                        list.remove(userId);
                        likedUsers = list.toArray(new String[0]);
                       // updates.put("like",String.valueOf(total) );
                        updates.put("liked_users", String.join(",",likedUsers));
                    } else {
                        List<String> list = new ArrayList<String>(Arrays.asList(likedUsers));
                        list.add(userId);
                        likedUsers = list.toArray(new String[0]);
                      //  updates.put("like",String.valueOf(total));
                        updates.put("liked_users", String.join(",",likedUsers));
                    }
                    ref.updateChildren(updates);
                }
            });

            reply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_reply);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.c_transparent);
                    final TextView replywith = (TextView) dialog.findViewById(R.id.replywith);
                    replywith.setText("Reply with "+profiles.get(position).getUser());
                    final EditText replyCommnet = (EditText) dialog.findViewById(R.id.et_replyCommnet);
                    dialog.findViewById(R.id.sendButtonDialog).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            long time = System.currentTimeMillis();
                            String comment_id = time+"_U_"+userId;
                                String replyCommnetString = replyCommnet.getText().toString().trim();
                                if (replyCommnetString.isEmpty()) {
                                    Toast.makeText(context, "Its empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("comments").child(profiles.get(position).getId()).child("replies").child(comment_id);
                                    Map<String, Object> updates = new HashMap<String,Object>();
                                    updates.put("comment",replyCommnetString);
                                    updates.put("user",userName);
                                    updates.put("user_img",user_img);
                                    ref.updateChildren(updates);
                                }


                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

        public void onPostExecute(int position,ArrayList<RetriverReplyComments> lists) {

            reference = FirebaseDatabase.getInstance().getReference().child("comments").child(profiles.get(position).getId()).child("replies");
            reference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        RetriverReplyComments pas = dataSnapshot1.getValue(RetriverReplyComments.class);
                        lists.add(pas);

                    }
                    /*replyComentsAdapter = new ReplyComentsAdapter((Activity) context,lists);
                    if (recyclerView != null) {*/
                    replyComentsAdapter = new ReplyComentsAdapter(context,lists);
                        recyclerView.setAdapter(replyComentsAdapter);
                    recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                                super.onScrolled(recyclerView, dx, dy);
                            }
                        });
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
//            recyclerView.setLayoutManager(gridLayoutManager);
                    //}

                    //replyComentsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}