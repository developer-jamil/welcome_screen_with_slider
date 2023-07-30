package com.jamillabltd.welcomescreensliderwithviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout linearLayout;
    TextView[] dostTv;
    int[] layouts;
    Button nextButton, skipButton;
    MyAdapter myAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isFirstTimeAPpStart()) {
            setAppStartStatus(false);
            startActivity(new Intent(MainActivity.this, Welcome.class));
            finish();
        }

        setContentView(R.layout.activity_main);


        viewPager = findViewById(R.id.viewPagerId);
        linearLayout = findViewById(R.id.dotsLayoutId);
        nextButton = findViewById(R.id.btn_next);
        skipButton = findViewById(R.id.btn_skip);

        //status bar
        statusBarTransparent();

        skipButton.setOnClickListener(v -> {

            setAppStartStatus(false);
            startActivity(new Intent(MainActivity.this, Welcome.class));
            finish();

        });

        nextButton.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem()+1;
            if (currentPage < layouts.length) {
                viewPager.setCurrentItem(currentPage);
            }else {

                setAppStartStatus(false);
                startActivity(new Intent(MainActivity.this, Welcome.class));
                finish();

            }
        });


        layouts = new int[] {R.layout.slide_1, R.layout.slide_2, R.layout.slide_3, R.layout.slide_4};
        myAdapter = new MyAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == layouts.length - 1) {
                    nextButton.setText("Start");
                    skipButton.setVisibility(View.GONE);
                }else {
                    nextButton.setText("Next");
                    skipButton.setVisibility(View.VISIBLE);
                }

                setDots(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDots(0);



    }

    private void statusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setDots(int page){
        linearLayout.removeAllViews();
        dostTv = new TextView[layouts.length];

        for (int i = 0; i<dostTv.length; i++){
            dostTv[i] = new TextView(this);
            dostTv[i].setText(Html.fromHtml("&#8226"));
            dostTv[i].setTextSize(30);
            dostTv[i].setTextColor(Color.parseColor("#a9b4bb"));
            linearLayout.addView(dostTv[i]);
        }

        if (dostTv.length > 0){
            dostTv[page].setTextColor(Color.parseColor("#ffffff"));
        }


    }

    private boolean isFirstTimeAPpStart(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SLIDER_APP", MODE_PRIVATE);
        return preferences.getBoolean("APP_START", true);
    }

    private void setAppStartStatus(boolean status){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SLIDER_APP", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("APP_START", status);
        editor.apply();
    }



}