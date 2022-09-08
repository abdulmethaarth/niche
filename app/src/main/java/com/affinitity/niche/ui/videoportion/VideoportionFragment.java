package com.affinitity.niche.ui.videoportion;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affinitity.niche.MainActivity;
import com.affinitity.niche.MyInterface;
import com.affinitity.niche.R;
import com.affinitity.niche.URLs;
import com.affinitity.niche.ui.maincategory.MainFragment;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VideoportionFragment extends Fragment {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Tracker tracker;
    private TextView topbarText,descriptonText;
    Button btShowmore,btnNext;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    private MainActivity mainActivity;
 public    String homeid,descData,videoID ;
    String videoUrl;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_videoportion, container, false);
        //     ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mainActivity      = (MainActivity) getActivity();
        playerView =root.findViewById(R.id.video_view);
        topbarText = (TextView) root.findViewById(R.id.topbarText);
        btShowmore = (Button) root.findViewById(R.id.btShowmore);
        descriptonText = (TextView) root.findViewById(R.id.descriptionText);
        btnNext = (Button)root.findViewById(R.id.buttonNext) ;

         descriptonText.setMaxLines(5);
        descriptonText.setText(descData);
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

        String id =    getArguments().getString("id");
        loadVideo(id);





        return  root;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();

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
    }

    @Override
    public void onStop() {
        super.onStop();
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
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                   bundle.putString("paramid", videoID);

                MainFragment mainFragment  = new MainFragment();
                mainFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mainFragment).commit();
            }
        });
        topbarText.setText(homeid);
        descriptonText.setText(descData);
        playerView.setPlayer(player);
     //   String myString = videoModel.getVideoFILE();
//    String easyPuzzle = i.getStringExtra("puzzle");
//    Bundle bundle = getIntent().getExtras();
//    String message = bundle.getString("urlString");
//        Bundle bundle = null;
//        bundle = this.getIntent().getExtras();
//        String myString = bundle.getString("urlString");

        Uri uri = Uri.parse(String.valueOf(videoUrl));
        MediaSource mediaSource = buildMediaSource(uri);




        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    // media actually playing
                    topbarText.setText(homeid);
                } else if (playWhenReady) {
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)
                } else {
                   // topbarText.setText("Pause");
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
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;

        }
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


    private void loadVideo(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface request = retrofit.create(MyInterface.class);

        Call<VideoModel> call = request.getVideo(id);
        call.enqueue(new Callback<VideoModel>() {


            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {

                VideoModel videoModel = response.body();

                Log.d("Success",""+videoModel.getVideoID());

                videoID = videoModel.getVideoID();
  homeid = videoModel.getHomeID();
                videoUrl = videoModel.getVideoFILE();
                descData = videoModel.getVideoDESC();

initializePlayer( );


            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {

            }


        });

    }
}

