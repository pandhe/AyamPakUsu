package com.pontianak.ayampakusu;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoadingActivity extends AppCompatActivity {
    ConstraintLayout lay_progress;
    Button bt_login,bt_daftar;
    private FirebaseAuth mAuth;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.firstloading);
        lay_progress=findViewById(R.id.lay_proggres);
        helper=new Helper(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            helper.meditor.putBoolean("is_login",false).apply();


        }
        else{
            helper.meditor.putBoolean("is_login",true);
            helper.meditor.putString("username",currentUser.getDisplayName()).apply();
            helper.meditor.putString("email",currentUser.getEmail()).apply();


        }
        Intent intent=new Intent(LoadingActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        LoadingActivity.this.finish();

    }
}
