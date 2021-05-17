package com.prashant.covidtracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences ;
    Boolean isVisited;
    TextView tvGetStarted;
    ImageButton getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();


        tvGetStarted = findViewById(R.id.tvGetStarted);
        getStartedBtn = findViewById(R.id.getStatedBtn);



        sharedPreferences  = getSharedPreferences("Covid19Tracker", MODE_PRIVATE);

        isVisited = sharedPreferences.getBoolean("isVisited" , false);


        if(isVisited){
            Intent i = new Intent(WelcomeActivity.this , MainActivity.class);
            startActivity(i);
            finish();

        }else{
            tvGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMain();
                }
            });

            getStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMain();
                }
            });
        }
    }
    private void gotoMain(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        isVisited = true;
        editor.putBoolean("isVisited" , true);
        editor.apply();

        Intent i = new Intent(WelcomeActivity.this , MainActivity.class);
        startActivity(i);
        finish();

    }
}