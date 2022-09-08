package com.affinitity.niche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.affinitity.niche.response.UpdateResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText editTextUsername;
    TextInputEditText editTextEmail;
    TextInputEditText editTextMobile;
    Button _signupButton;
    TextView _alreadyLoginLink;
    String username;
    String email;
    String mobilr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        _signupButton = findViewById(R.id.btn_signup);
        _alreadyLoginLink  = findViewById(R.id.link_signup);
        editTextUsername  = findViewById(R.id.input_name);
        editTextEmail  =  findViewById(R.id.input_email);
        editTextMobile  =  findViewById(R.id.input_mobile);



        _alreadyLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignup();
                //  Toast.makeText(LoginActivity.this, "elllo", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void userSignup() {
       username = editTextUsername.getText().toString();
       email = editTextEmail.getText().toString();
       mobilr = editTextMobile.getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }
        else if (editTextEmail.getText().toString().trim().length() != 0 && !isValidEmail(editTextEmail.getText().toString().trim())){
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(mobilr)) {
            editTextMobile.setError("Please enter your phone number");
            editTextMobile.requestFocus();
            return;
        }
        else{
            signUpMethod();
        }

}

    private void signUpMethod() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hornbillfoundation.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface  api = retrofit.create(MyInterface.class);
        Call<SignUpUser> call =  api.signupUser(username,email,mobilr);

        call.enqueue(new Callback<SignUpUser>() {
            @Override
            public void onResponse(Call<SignUpUser> call, Response<SignUpUser> response) {

                SignUpUser user = response.body();
                if(user.getStatus().equalsIgnoreCase("true")){
                    Toast.makeText(SignUpActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Sorry "+user.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpUser> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}