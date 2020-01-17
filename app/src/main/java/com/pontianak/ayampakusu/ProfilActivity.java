package com.pontianak.ayampakusu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends KuberlayarDilautan {
    private Helper helper;
    private Service_Connector service_connector;
    private Button bt_daftar,bt_daftar_google;
    private TextInputEditText edt_email,edt_tanggal_lahir,edt_nama,edt_no_telp,edt_ulang_password,edt_username;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    MyConfig myConfig;
    ImageView img_profil;
    public Calendar myCalendar;
    TextView txt_akun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        bt_daftar=findViewById(R.id.bt_daftar);
        myConfig=new MyConfig();
        edt_no_telp=findViewById(R.id.edt_no_telp);
        edt_ulang_password=findViewById(R.id.edt_ulang_password);
        edt_email=findViewById(R.id.edt_email);
        edt_tanggal_lahir=findViewById(R.id.edt_tanggal_lahir);
        edt_username=findViewById(R.id.edt_username);
        img_profil=findViewById(R.id.img_profil3);
        myCalendar = Calendar.getInstance();
        txt_akun=findViewById(R.id.txt_akun2);

        mAuth = FirebaseAuth.getInstance();
        helper=new Helper(this);
        service_connector=new Service_Connector();
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        txt_akun.setText(currentUser.getEmail());
        Glide.with(ProfilActivity.this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(img_profil);




        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            dialog_loading.show();
                            String idToken = task.getResult().getToken();

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("idtoken", idToken);
                            params.put("no_telp",edt_no_telp.getText().toString());
                            params.put("tanggal_lahir",edt_tanggal_lahir.getText().toString());
                            params.put("username",edt_username.getText().toString());

                            service_connector.sendpostrequest(ProfilActivity.this, "setprofil", params, new Service_Connector.VolleyResponseListener_v3() {
                                @Override
                                public void onError(String message) {
                                    dialog_loading.hide();

                                }

                                @Override
                                public void onResponese(String response) {
                                    dialog_loading.hide();
                                    try{
                                        JSONObject respon=new JSONObject(response);
                                        if(respon.getString("status").equals("1")){
                                            setResult(RESULT_OK);
                                           finish();

                                        }
                                        else{

                                        }
                                    }
                                    catch (JSONException joe){
                                        Log.i("ez",joe.getMessage());

                                    }
                                }

                                @Override
                                public void onNoConnection(String message) {
                                    dialog_loading.hide();

                                }

                                @Override
                                public void OnServerError(String message) {
                                    dialog_loading.hide();
                                }

                                @Override
                                public void OnTimeOut() {
                                    dialog_loading.hide();
                                }
                            });
                        } else {
                            // Handle error -> task.getException();
                            Log.i("ez", task.getException().getMessage());
                        }

                    }
                });

            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        edt_tanggal_lahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(ProfilActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                } else {
                    // Hide your calender here
                }
            }
        });

        getuser();




    }
    private void updateLabel() {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_tanggal_lahir.setText(sdf.format(myCalendar.getTime()));
    }
    private void getuser(){
        dialog_loading.show();
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);

                    service_connector.sendpostrequest(ProfilActivity.this, "getprofil", params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {
                            dialog_loading.hide();

                        }

                        @Override
                        public void onResponese(String response) {
                            dialog_loading.hide();
                            try{
                                JSONObject respon=new JSONObject(response);
                                if(respon.getString("status").equals("1")){
                                    JSONObject USR=new JSONObject(respon.getString("data"));
                                    edt_email.setText(USR.getString("email"));
                                    edt_no_telp.setText(USR.getString("no_telp"));
                                    edt_username.setText(USR.getString("username"));
                                    edt_tanggal_lahir.setText(USR.getString("tanggal_lahir_pelanggan"));
                                    if(USR.getString("foto_profil").equals("null")){
                                        Glide.with(ProfilActivity.this).load(myConfig.main_url+"/images/ic_account_n.png").apply(RequestOptions.circleCropTransform()).into(img_profil);

                                    }
                                    else{
                                        Glide.with(ProfilActivity.this).load(USR.getString("foto_profil")).apply(RequestOptions.circleCropTransform()).into(img_profil);

                                    }

                                }
                                else{

                                }
                            }
                            catch (JSONException joe){
                                Log.i("ez",joe.getMessage());

                            }
                        }

                        @Override
                        public void onNoConnection(String message) {
                            dialog_loading.hide();

                        }

                        @Override
                        public void OnServerError(String message) {
                            dialog_loading.hide();

                        }

                        @Override
                        public void OnTimeOut() {
                            dialog_loading.hide();

                        }
                    });
                } else {
                    // Handle error -> task.getException();
                    Log.i("ez", task.getException().getMessage());
                    dialog_loading.hide();
                }

            }
        });



    }
}
