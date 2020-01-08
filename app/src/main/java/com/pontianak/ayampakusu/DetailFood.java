package com.pontianak.ayampakusu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

public class DetailFood extends AppCompatActivity {
    WebView myWebView;
    ImageView img_detail_food;
    TextView txt_nama_food,txt_harga_food;
    MyConfig myConfig;
    Service_Connector service_connector;
    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        CookieManager cookieManager = CookieManager.getInstance();
        myConfig=new MyConfig();
        img_detail_food=findViewById(R.id.img_detail_food);
        txt_harga_food=findViewById(R.id.txt_harga_food);
        txt_nama_food=findViewById(R.id.txt_nama_food);
        helper=new Helper(this);

        myWebView =findViewById(R.id.web_food);
        WebSettings webSettings = myWebView.getSettings();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(myWebView,true);
        }
        else { cookieManager.setAcceptCookie(true);
        }
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("ez",String.valueOf(newProgress));



            }

        });
        Intent intent=getIntent();

        myWebView.loadUrl(intent.getStringExtra("url"));
        Glide.with(this).load(myConfig.main_url + "images/food/" + intent.getStringExtra("foto")).into(img_detail_food);
        txt_harga_food.setText(helper.formattorupiah(intent.getStringExtra("harga")));
        txt_nama_food.setText(intent.getStringExtra("nama"));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
