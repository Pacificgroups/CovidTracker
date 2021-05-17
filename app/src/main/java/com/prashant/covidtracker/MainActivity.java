package com.prashant.covidtracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    private LinearLayout safetyLayout , countryChangeLayout;
    private ImageButton safetyBtn;
    private String countryName;

    private ImageView countryFlagImg;

    private TextView tvCases,
            tvRecovered,
            tvActive,
            tvTodayCases,
            tvDeaths,
            tvTodayDeaths,
            tvTodayRecovered,
            tvCountryTab,
            tvGlobalTab;

    Double totalCases,
            todayCases,
            totalRecovered,
            todayRecovered,
            totalDeaths,
            todayDeaths,
            totalActive;


    public final int countryTab = 0;
    public final int globalTab = 1;


    private int currentTab;

    private ShimmerFrameLayout tvCasesShimmer,
            tvRecoveredShimmer,
            tvActiveShimmer,
            tvTodayCasesShimmer,
            tvDeathsShimmer,
            tvTodayDeathsShimmer,
            tvTodayRecoveredShimmer;

    @Override
    protected void onStart() {
        super.onStart();
        loadUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUI();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("activeTabIndex" , currentTab);
        editor.putString("countryName" , countryName);
        editor.apply();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //sharedPref
        preferences = getSharedPreferences("Covid19Tracker" , MODE_PRIVATE);

        //getting countryCode from sharedPref
        countryName = preferences.getString("countryName" , "India");

        // getting tabIndex;
        currentTab = preferences.getInt("activeTabIndex" , globalTab);

        safetyLayout = findViewById(R.id.symptomsLayout);
        safetyBtn = findViewById(R.id.symptomsBtn);

        countryChangeLayout = findViewById(R.id.countryChangeLayout);
        countryFlagImg = findViewById(R.id.countryFlagImg);

        tvCases = findViewById(R.id.tvConfirmed);
        tvTodayCases = findViewById(R.id.tvTodayConfirmed);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvTodayRecovered = findViewById(R.id.tvTodayRecovered);
        tvActive = findViewById(R.id.tvActive);
        tvDeaths = findViewById(R.id.tvDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvCountryTab = findViewById(R.id.countryTab);
        tvGlobalTab = findViewById(R.id.globalTab);

        //Shimmer layout;
        tvCasesShimmer = findViewById(R.id.tvConfirmedShimmer);
        tvTodayCasesShimmer = findViewById(R.id.tvTodayConfirmedShimmer);
        tvActiveShimmer = findViewById(R.id.tvActiveShimmer);
        tvRecoveredShimmer = findViewById(R.id.tvRecoveredShimmer);
        tvTodayRecoveredShimmer = findViewById(R.id.tvTodayRecoveredShimmer);
        tvDeathsShimmer = findViewById(R.id.tvDeathsShimmer);
        tvTodayDeathsShimmer = findViewById(R.id.tvTodayDeathsShimmer);

        //load data and ui

        loadUI();


        //safety Layout and btn on click goto to Symptoms Activity

        safetyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSafetyActivity();
            }
        });

        safetyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSafetyActivity();
            }
        });

        countryChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });

        tvCountryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTab = countryTab;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("activeTabIndex" , currentTab);
                editor.apply();
                loadUI();
            }
        });

        tvGlobalTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTab = globalTab;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("activeTabIndex" , currentTab);
                editor.apply();
                loadUI();

            }
        });

    }

    private void loadUI(){

        countryName = preferences.getString("countryName" , "India");

        loadData();

        if(currentTab == countryTab){
            tvCountryTab.setTextColor(Color.parseColor("#142237"));
            tvCountryTab.setBackgroundResource(R.drawable.country_back);
            tvGlobalTab.setTextColor(Color.parseColor("#c8815b"));
            tvGlobalTab.setBackgroundColor(Color.parseColor("#00000000"));

        }else{
            tvGlobalTab.setTextColor(Color.parseColor("#142237"));
            tvGlobalTab.setBackgroundResource(R.drawable.country_back);
            tvCountryTab.setTextColor(Color.parseColor("#c8815b"));
            tvCountryTab.setBackgroundColor(Color.parseColor("#00000000"));

        }


    }

    private void loadData(){
        if(currentTab == countryTab){
            fetchData("https://corona.lmao.ninja/v3/covid-19/countries/" + countryName);
        }else{
            fetchData("https://corona.lmao.ninja/v3/covid-19/all/");
        }

    }


    private void fetchData(String url){


        tvCasesShimmer.showShimmer(true);
        tvTodayCasesShimmer.showShimmer(true);
        tvActiveShimmer.showShimmer(true);
        tvRecoveredShimmer.showShimmer(true);
        tvTodayRecoveredShimmer.showShimmer(true);
        tvDeathsShimmer.showShimmer(true);
        tvTodayDeathsShimmer.showShimmer(true);


        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            totalCases = Double.parseDouble(jsonObject.getString("cases"));
                            todayCases = Double.parseDouble(jsonObject.getString("todayCases"));
                            totalRecovered = Double.parseDouble(jsonObject.getString("recovered"));
                            todayRecovered = Double.parseDouble(jsonObject.getString("todayRecovered"));
                            totalDeaths = Double.parseDouble(jsonObject.getString("deaths"));
                            todayDeaths = Double.parseDouble(jsonObject.getString("todayDeaths"));
                            totalActive = Double.parseDouble(jsonObject.getString("active"));




                            if(currentTab == countryTab){

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                                Glide.with(getApplicationContext()).load(flagUrl).placeholder(R.drawable.worldwide).circleCrop().into(countryFlagImg);
                            }else{
                                Glide.with(getApplicationContext()).load(R.drawable.worldwide).into(countryFlagImg);
                            }

                            tvCasesShimmer.hideShimmer();
                            tvTodayCasesShimmer.hideShimmer();
                            tvActiveShimmer.hideShimmer();
                            tvRecoveredShimmer.hideShimmer();
                            tvTodayRecoveredShimmer.hideShimmer();
                            tvDeathsShimmer.hideShimmer();
                            tvTodayDeathsShimmer.hideShimmer();


                            tvCases.setText(convert(totalCases));
                            tvTodayCases.setText("+ "+convert(todayCases));
                            tvRecovered.setText(convert(totalRecovered));
                            tvTodayRecovered.setText("+ "+convert(todayRecovered));
                            tvActive.setText(convert(totalActive));
                            tvDeaths.setText(convert(totalDeaths));
                            tvTodayDeaths.setText("+ "+convert(todayDeaths));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private String convert(Double n){
        if(n < 1000){
            return String.format("%.0f",n);
        }else if(n <100000){
            n = n / 1000;
            return String.format("%.2f K",n);
        }else if(n < 10000000){
            n = n / 100000;
            return String.format("%.2f L",n);
        }else{
            n = n / 10000000;
            return String.format("%.2f Cr",n);
        }
    }

    private void gotoSafetyActivity(){
        startActivity(new Intent(MainActivity.this,SymptomsActivity.class));
    }
}