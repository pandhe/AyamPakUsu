package com.pontianak.ayampakusu;

import android.content.Intent;


import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SlideLoginActivity extends AppCompatActivity {
    private ViewPager pagerslider;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button bt_daftar,bt_masuk;
    private int[] layouts;
    final ArrayList<String>images=new ArrayList<>();
    final ArrayList<String>detail=new ArrayList<>();
    private TheConfig theConfig=new TheConfig();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_login);
        pagerslider=findViewById(R.id.pagerslider);
        dotsLayout = findViewById(R.id.layoutDots);
        bt_daftar=findViewById(R.id.bt_daftar);
        bt_masuk=findViewById(R.id.bt_masuk);
        //getSupportActionBar().setTitle("Bergabung Menjadi Member");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);


        images.add("download");
        images.add("enak");
        images.add("makan");

        detail.add("Scan Barcode dari kasir dengan smartphone android anda sekarang, dan dapatkan aneka diskon dan voucher dari kami");
        detail.add("Dapatkan aneka diskon dan vocer dari Ayam Pak Usu Pontianak");
        detail.add("Semua promo juga berlaku di semua outlet Pondok Ale Ale");

        pagerslider.addOnPageChangeListener(viewPagerPageChangeListener);
        SlidePagerAdapter slideadapter=new SlidePagerAdapter(this,images,detail);
        pagerslider.setAdapter(slideadapter);
        addBottomDots(0);
        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SlideLoginActivity.this,DaftarActivity.class);
                startActivityForResult(intent,theConfig.REQUEST_DAFTAR);
            }
        });

        bt_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SlideLoginActivity.this,LoginActivity.class);
                startActivityForResult(intent,theConfig.REQUEST_LOGIN);
            }
        });


    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[images.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private int getItem(int i) {
        return pagerslider.getCurrentItem() + i;
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == images.size() - 1) {
                // last page. make button text to GOT IT
                //btnNext.setText(getString(R.string.start));
               // btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
               // btnNext.setText(getString(R.string.next));
              //  btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent returnIntent = new Intent();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==theConfig.REQUEST_DAFTAR){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                this.finish();
            }

        }
        else if(requestCode==theConfig.REQUEST_LOGIN){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}
