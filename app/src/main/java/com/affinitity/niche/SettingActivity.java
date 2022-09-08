package com.affinitity.niche;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.affinitity.niche.models.User;
import com.affinitity.niche.pref.SharedPrefManager;
import com.affinitity.niche.response.UpdateResponse;
import com.affinitity.niche.ui.settings.ImagePickerActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import es.dmoral.toasty.Toasty;

public class SettingActivity  extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    @BindView(R.id.img_profile)
    ImageView imgProfile;
    ProgressDialog progressDialog;
    Button btnsubmit;

    Context context;

    TextInputEditText input_username, input_email,input_password,input_password2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODES = 101;
    Bitmap bitmap;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    boolean connected = false;

    ImageView back_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(SettingActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        btnsubmit =  (Button)  findViewById(R.id.btnsubmit);
        input_username = findViewById(R.id.input_username);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_password2 = findViewById(R.id.input_password2);
        back_icon = findViewById(R.id.back_icon);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String isString = String.valueOf(user.getId());
        loadProfileDefault(isString);

        final String password  =   input_password.getText().toString();
        final String passwordtwo = input_password2.getText().toString();

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnsubmit.setOnClickListener(v -> {
                    updatedata(isString);

         });
    }


    private void updatedata(String id) {

        final String username = input_username.getText().toString();
        final String email  = input_email.getText().toString();
        final String password  =   input_password.getText().toString();
        final String passwordtwo = input_password2.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            input_username.setError("Please enter your username");
            input_username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            input_email.setError("Please enter your Email id");
            input_email.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface  api = retrofit.create(MyInterface.class);
        Call<UpdateResponse>call =  api.insertdata(username,password,email,id);

    call.enqueue(new Callback<UpdateResponse>() {
        @Override
        public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
            UpdateResponse updateResponse = response.body();
            Log.d("msg","______________________________________________________"+updateResponse.getMessage());
            Toast.makeText(SettingActivity.this, "Successs"+updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }

        @Override
        public void onFailure(Call<UpdateResponse> call, Throwable t) {
            Toast.makeText(SettingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }


    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url).placeholder(R.drawable.animaterotate)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
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

                        input_username.setText(settingResponse.getUsername());
                        input_email.setText(settingResponse.getEmail());
                    //    Url uri = settingResponse.getImage();
                        loadProfile(settingResponse.getImage().toString());
//                        Glide.with(context).
//                                load(settingResponse.getImage())
//                                .error(R.drawable.baseline_account_circle_black_48)
//                                .into(imgProfile);
                     //   imgProfile.setColorFilter(ContextCompat.getColor(context, R.color.profile_default_tint));
                    }

                } else {
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));

                }






            }

            @Override
            public void onFailure(Call<settingResponse> call, Throwable t) {

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.img_plus, R.id.img_profile})
    void onProfileImageClick() {
       /* if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }*/
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void launchCameraIntent() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODES);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(SettingActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODES) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        //Driver IMage
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            // driver_img.setImageBitmap(driverbitmap);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";
                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                String filePath = imageFile.getPath();
                Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                imgProfile.setImageBitmap(ssbitmap);
                uploadImg(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode ==  REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Toast.makeText(SettingActivity.this, "Driver Image Saved!", Toast.LENGTH_SHORT).show();
                imgProfile.setImageBitmap(bitmap);
                String filePath = ImageFilePath.getPath(SettingActivity.this, data.getData());
                uploadImg(filePath);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SettingActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImg(String path) {

        ImagePickerActivity.clearCache(this);

        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setCancelable(false);
        //  dialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        //  progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // progressDialog.setProgress(0);
        progressDialog.setMax(100);
        // progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        User user = SharedPrefManager.getInstance(this).getUser();
        String ID = String.valueOf(user.getId());

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            try {
                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                new MultipartUploadRequest(SettingActivity.this, uploadId, "http://hornbillfoundation.org/api/Api.php?apicall=uploadUserImage")
                        .addFileToUpload(path, "file") //Adding file
                        .addParameter("id", ID)
                        .setMaxRetries(3)
                        .startUpload();//Starting the upload
                loadProfileDefault(ID);
                Intent intent = new Intent(SettingActivity.this,SettingActivity.class);
                startActivity(intent);
            } catch (FileNotFoundException f) {
                //   Common.showMkError(SignUpActivity.this, getResources().getString(R.string.check_internet));
                Toast.makeText(SettingActivity.this, "check internet", Toast.LENGTH_SHORT).show();
            } catch (Exception exc) {
            }
        }
        else{
            connected = false;
            Snackbar.make(findViewById(android.R.id.content), "Check your internet connection.", Snackbar.LENGTH_LONG).show();
        }
        /*MyInterface api = retrofit.create(MyInterface.class);
        Call<MyResponse> call = api.uploadImage(fileToUpload,ID);
        call.enqueue(new Callback<MyResponse>() {

            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
              //  Toast.makeText(SettingActivity.this, "ONRESPONSE", Toast.LENGTH_SHORT).show();
            //    Log.d("Message","Misfer"+response.body().message);
                if (!response.body().isError()) {
                    progressDialog.dismiss();
                    Toast.makeText(SettingActivity.this, "File Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                //    Toasty.success(SettingActivity.this, "File Uploaded Successfully... ", Toast.LENGTH_LONG, true).show();

//                  Toast toast   =  Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {
//                    Toast toast  =
//                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Toast.makeText(SettingActivity.this, "Some error occurred... ", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
//                Toast toast  =     Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                Toast.makeText(SettingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });*/
    }



    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
}
}
