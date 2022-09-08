package com.affinitity.niche.ui.videoPlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.affinitity.niche.ConstantsStored;
import com.affinitity.niche.MainActivity;
import com.affinitity.niche.R;
import com.affinitity.niche.RetriverComments;
import com.affinitity.niche.UserDetails;
import com.affinitity.niche.pref.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class VideoPlayerFragment extends Fragment {
    private PlayerView playerView;
    ProgressBar mProgressBar;
    private SimpleExoPlayer player;
    String myString,videoID,videoName,catogoryName,description,imageUrl,userName,user_img,userId;
    String like = "0";
    long resumePosition;
    int zero = 0;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Tracker tracker;
    private TextView topbarText,descriptonText;
  Button btShowmore;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    private MainActivity mainActivity;

    Firebase reference1, reference2;
    /*LinearLayout layout;
    RelativeLayout layout_2;*/
    ImageView cmtsendButton;
    EditText messageArea;
  //  ScrollView scrollView;
  RecyclerView  recyclerView;
    DatabaseReference reference;
    ArrayList<RetriverComments> list;
    ArrayList<RetriverReplyComments> lists;
    CommentsAdapter adapter;
    ArrayList <Map<String, String> > comments = new ArrayList<Map<String, String>>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video_player, container, false);

        recyclerView = (RecyclerView)
                root.findViewById(R.id.rv_commnets_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
/*
        mProgressBar = (ProgressBar) root.findViewById(R.id.Progressbar);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);*/

        SharedPreferences prefs = getActivity().getSharedPreferences(ConstantsStored.MY_PREFS_NAME, MODE_PRIVATE);
        userName = prefs.getString(ConstantsStored.name, "");
        user_img = prefs.getString(ConstantsStored.user_img, "");
        userId = prefs.getString(ConstantsStored.user_id, "");

        SharedPreferences prefrns = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE);
        String s = prefrns.getString(Constant.playbackPositionLastplayed, "");
        if(s.isEmpty()){

        }else{
            currentWindow = Integer.parseInt(prefrns.getString(Constant.playbackCurrentWindowIndex, ""));
            playbackPosition = Long.parseLong(prefrns.getString(Constant.playbackPositionLastplayed, ""));
        }

        mainActivity      = (MainActivity) getActivity();
         playerView =root.findViewById(R.id.video_view);
        topbarText = (TextView) root.findViewById(R.id.topbarText);
        btShowmore = (Button) root.findViewById(R.id.btShowmore);
        descriptonText = (TextView) root.findViewById(R.id.descriptionText);
        String description =    getArguments().getString("description");
        descriptonText.setMaxLines(4);
        descriptonText.setText(description);

        btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btShowmore.getText().toString().equalsIgnoreCase("Read More...")) {
                    descriptonText.setMaxLines(Integer.MAX_VALUE);
                    btShowmore.setText("Show Less...");
                } else {
                    descriptonText.setMaxLines(4);
                    btShowmore.setText("Read More...");

                }
            }
        });



        BottomNavigationView navBar = mainActivity.findViewById(R.id.bottom_nav_view);
        LinearLayout messagebox = mainActivity.findViewById(R.id.edit_coment_box);
        cmtsendButton = mainActivity.findViewById(R.id.cmtsendButton);
        messageArea = mainActivity.findViewById(R.id.messageArea);
        messagebox.setVisibility(View.VISIBLE);


        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(view -> {

            if(fullscreen) {

                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(getContext() ,R.drawable.ic_fullscreen));
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if((mainActivity).getSupportActionBar() != null){

                    mainActivity.getSupportActionBar().show();
                    navBar.setVisibility(View.VISIBLE);
                  //  navBar.setVisibility(View.GONE);

                }
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) ( 200 * getActivity().getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
                fullscreen = false;
            }else{
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen));
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                if( mainActivity.getSupportActionBar() != null){
                    mainActivity.getSupportActionBar().hide();
                    navBar.setVisibility(View.GONE);
                }
               getActivity(). setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams(params);
                fullscreen = true;
            }
        });

        Firebase.setAndroidContext(getActivity());
        reference1 = new Firebase("https://niche-1bd5f.firebaseio.com/comments/");


        cmtsendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();
                String comment_id = time+"_U_"+userId;
               String VideoID = getArguments().getString("videoId");
                String messageText = messageArea.getText().toString();
                reference2 = new Firebase("https://niche-1bd5f.firebaseio.com/comments/"+comment_id);

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("videoId", videoID);
                    map.put("like", "");
                    map.put("comment", messageText);
                    map.put("user", userName);
                    map.put("liked_users", String.valueOf(0));
                    map.put("creates_at", String.valueOf(time));
                    map.put("updated_at", String.valueOf(time));
                    map.put("user_img", user_img);
                    map.put("comment_ids","");
                    map.put("replies","");
                    map.put("inner_comment","");
                    map.put("type","video");
                    map.put("reply_comment_id",comment_id);
                    map.put("id",comment_id);

                    reference2.setValue(map);
                    messageArea.setText("");

                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("comments");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                list = new ArrayList<RetriverComments>();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    RetriverComments p = dataSnapshot1.getValue(RetriverComments.class);
                    list.add(p);
                }
                adapter = new CommentsAdapter(getActivity(),list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return  root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
      /*  if (player == null) {
            initializePlayer();
        }
        else{
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();

        if (player == null) {
           initializePlayer();
        }
        else{
            releasePlayer();
        }
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        if (player == null) {
            initializePlayer();
        }
        else{
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        }

        topbarText.setText("Misfer");

        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
      //  new MyAsync().execute();
         myString = getArguments().getString("urlString");
         videoID = getArguments().getString("videoId");
        videoName = getArguments().getString("videoName");
        catogoryName = getArguments().getString("catogoryName");
        description = getArguments().getString("description");
        imageUrl = getArguments().getString("imagurl");

        Uri uri = Uri.parse(myString);
        MediaSource mediaSource = buildMediaSource(uri);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    // media actually playing
                    topbarText.setText("Playing");
                } else if (playWhenReady) {
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)
                } else {
                    topbarText.setText("Pause");
                    // player paused in any state
                }
            }
        });

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(Constant.playbackPositionLastplayed, String.valueOf(player.getCurrentPosition()));
            editor.putString(Constant.playbackCurrentWindowIndex, String.valueOf(player.getCurrentWindowIndex()));
            editor.apply();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;


        }
    }

    private SharedPreferences getSharedPreferences(String myPrefsName, int modePrivate) {
        return getActivity().getSharedPreferences(myPrefsName, modePrivate);
    }

    private void updateResumePosition() {
        currentWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getActivity(),"ua"),
                new DefaultExtractorsFactory(), null, null);

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );

    }
    }

