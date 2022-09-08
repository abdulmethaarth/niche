package com.affinitity.niche;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.affinitity.niche.models.User;
import com.affinitity.niche.pref.SharedPrefManager;
import com.affinitity.niche.ui.dashboard.DashboardFragment;
import com.affinitity.niche.ui.home.HomeFragment;
import com.affinitity.niche.ui.mainhome.MainhomeFragment;
import com.affinitity.niche.ui.pdf.PdfFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity  extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;

    TextView textView,exp_date;
   CircularImageView circularImageView;
    LinearLayout messagebox;
    ImageView cmtsendButton;
    EditText messageArea;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  initToolbar();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
     //   initNavigation();
        //showBottomNavigation(false);

        messagebox  = findViewById(R.id.edit_coment_box);
        cmtsendButton =findViewById(R.id.cmtsendButton);
        messageArea = findViewById(R.id.messageArea);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainhomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        textView = (TextView) headerView.findViewById(R.id.textView);
        exp_date = (TextView) headerView.findViewById(R.id.exp_date);
        circularImageView = (CircularImageView) headerView.findViewById(R.id.img_profile);

//        btn   = (Button) headerView.findViewById(R.id.buttonRadio);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();


        String isString = String.valueOf(user.getId());

        loadProfileDefault(isString);


    }


    private void loadProfileDefault(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface  request = retrofit.create(MyInterface.class);
        Call<settingResponse> call = request.getUserDetails(id);
        call.enqueue(new Callback<settingResponse>() {


            @Override
            public void onResponse(Call<settingResponse> call, Response<settingResponse> response) {

                settingResponse  settingResponse = response.body();

                if(settingResponse.isLogin()) {
                    if(settingResponse.getStatus().equals("active")) {
                        textView.setText(settingResponse.getUsername());
                        exp_date.setText(settingResponse.getExpire());
                        SharedPreferences.Editor editor = getSharedPreferences(ConstantsStored.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(ConstantsStored.user_img, settingResponse.getImage());
                        editor.apply();
//                        input_username.setText(settingResponse.getUsername());
//                        input_email.setText(settingResponse.getEmail());
//                        //    Url uri = settingResponse.getImage();
//                        loadProfile(settingResponse.getImage().toString());
                        Glide.with(getApplicationContext()).
                                load(settingResponse.getImage())
                                .error(R.drawable.baseline_account_circle_black_48)
                                .into(circularImageView);
                        //   imgProfile.setColorFilter(ContextCompat.getColor(context, R.color.profile_default_tint));
                    }

                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }

            }

            @Override
            public void onFailure(Call<settingResponse> call, Throwable t) {

            }
        });



    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.bottom_home:
                            selectedFragment = new MainhomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();

                            messagebox.setVisibility(View.GONE);

                            break;
                        case R.id.bottom_search:

                            messagebox.setVisibility(View.GONE);
                           // selectedFragment = new FavoritesFragment();
                           // Toast.makeText(MainActivity.this, "Hello Seach", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                            startActivity(intent);

                            selectedFragment = new DashboardFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            break;
                        case R.id.bottom_notifications:
                          //  selectedFragment = new SearchFragment();
                            messagebox.setVisibility(View.GONE);
                            break;

                        case R.id.bottom_pdf:
                            selectedFragment = new PdfFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                            messagebox.setVisibility(View.GONE);

                            break;
                    }

                    return true;
                }
            };



    /*private void initFab() {

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Hello Misfer", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }*/

    private void initNavigation() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
      //  bottomNavView = findViewById(R.id.bottom_nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send,
//                R.id.bottom_home, R.id.bottom_dashboard, R.id.bottom_notifications)
//                .setDrawerLayout(drawer)
//                .build();
   //     navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
     //   NavigationUI.setupWithNavController(bottomNavView, navController);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment seletDrawer = null;
        switch (item.getItemId()) {
               /* case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainhomeFragment()).commit();
                break;*/

            case R.id.nav_setting:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;

            /*case R.id.nav_search:
                // selectedFragment = new FavoritesFragment();
                // Toast.makeText(MainActivity.this, "Hello Seach", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                            startActivity(intent);

                seletDrawer = new DashboardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        seletDrawer).commit();
                break;
            case R.id.nav_pdf:
                seletDrawer = new PdfFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        seletDrawer).commit();
                break;

            case R.id.nav_home_drawer:
                seletDrawer = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        seletDrawer).commit();
                break;
            case R.id.nav_videos:
             //   MainFragment
                seletDrawer = new MainhomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        seletDrawer).commit();
                break;*/
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout.")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                SharedPrefManager.getInstance(getApplicationContext()).logout();
                            }
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//    }
//

//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit.")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //  HomeActivity.super.onBackPressed();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        System.exit(0);
                    }
                }).create().show();
    }

    private void showBothNavigation(boolean isShowable) {
        navigationView.setVisibility(isShowable? View.VISIBLE : View.GONE);
        bottomNavView.setVisibility(isShowable? View.VISIBLE : View.GONE);
    }

    private void showBottomNavigation(boolean isShowable) {
        bottomNavView.setVisibility(isShowable? View.VISIBLE : View.GONE);
    }


}
