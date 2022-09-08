package com.affinitity.niche;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.affinitity.niche.models.User;
import com.affinitity.niche.pref.SharedPrefManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splash extends AppCompatActivity {
    Animation anim;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }*/

        imageView=(ImageView)findViewById(R.id.imageView2); // Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();


                String isString = String.valueOf(user.getId());


                    findUserData(isString);



                // HomeActivity.class is the activity to go after showing the splash screen.
            }



            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(anim);
    }


    private void findUserData(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface  request = retrofit.create(MyInterface.class);
        Call<SpashResponse> call = request.getUserData(id);
        call.enqueue(new Callback<SpashResponse>() {


            @Override
            public void onResponse(Call<SpashResponse> call, Response<SpashResponse> response) {

                SpashResponse spashResponse = response.body();

               if(spashResponse.isLogin()) {
   if(spashResponse.getStatus().equals("active")) {
       if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

           startActivity(new Intent(getApplicationContext(), MainActivity.class));

       } else {
           startActivity(new Intent(getApplicationContext(), LoginActivity.class));
       }
   } else {
       startActivity(new Intent(getApplicationContext(), ExpireActivity.class));
   }
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }

            }

            @Override
            public void onFailure(Call<SpashResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check your internet", Toast.LENGTH_SHORT).show();
            }
        });

    }


}