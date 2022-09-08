package com.affinitity.niche;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        String myString = bundle.getString("urlString");

        textView = (TextView) findViewById(R.id.textView);

        textView.setText(myString);


    }
}
