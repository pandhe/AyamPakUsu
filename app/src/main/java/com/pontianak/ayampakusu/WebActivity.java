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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    WebView myWebView;
    ProgressBar pg;
    MyConfig myConfig=new MyConfig();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        CookieManager cookieManager = CookieManager.getInstance();

        myWebView =findViewById(R.id.webview);
        pg=findViewById(R.id.progressBar);
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


                pg.setProgress(newProgress);
                if(newProgress==100){
                    pg.setVisibility(View.GONE);
                }
                else{
                    pg.setVisibility(View.VISIBLE);
                }
            }

        });
        TextView txt_judul=findViewById(R.id.txt_judul);
        ImageView imageview3=findViewById(R.id.imageView3);

        Intent intent=getIntent();
        txt_judul.setText(intent.getStringExtra("judul"));
        Glide.with(this).load(intent.getStringExtra("gambar") ).into(imageview3);


        myWebView.loadUrl(intent.getStringExtra("url"));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        else{
            myWebView.destroy();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
