package com.pontianak.ayampakusu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.ayampakusu.adapter.Kode_voucher;
import com.pontianak.ayampakusu.adapter.ParcelVoucherTukar;
import com.pontianak.ayampakusu.adapter.VoucherPelanggan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailVouchertukarActivity extends AppCompatActivity {
    ImageView img_1;
    TextView txt_1,txt_2,txt_3,txt_4;
    MyConfig myConfig;
    AlertDialog.Builder builder1;
    AlertDialog dialog1;
    View mydialog ;
    LayoutInflater gabung2;
    TextView txt_id,txt_nominal;
    ParcelVoucherTukar voucherTukar;
    Service_Connector service_connector;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Gson gson;
    Helper helper;
    int Currentpoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vouchertukar);
        voucherTukar=getIntent().getParcelableExtra("item");
        myConfig=new MyConfig();
        helper=new Helper(this);
        service_connector=new Service_Connector();
        txt_1=findViewById(R.id.txt_1);
        txt_2=findViewById(R.id.txt_2);
        txt_3=findViewById(R.id.txt_3);
        txt_4=findViewById(R.id.txt_4);
        img_1=findViewById(R.id.img_1);
        txt_1.setText(voucherTukar.nama_voucher);
        gabung2 =getLayoutInflater();
        txt_2.setText(voucherTukar.besaran_voucher +" Poin");
        txt_3.setText("Berakhir pada : "+helper.caritanggal(voucherTukar.datetime_stop_voucher));
        Glide.with(this).load(myConfig.main_url + "images/voucher/" + voucherTukar.image_voucher).into(img_1);
        builder1 = new AlertDialog.Builder(this);
        // builder.setTitle("Transaksi Point");
        mydialog = gabung2.inflate(R.layout.popup_konfirmasi_voucher, null);
        builder1.setView(mydialog);
        txt_id=mydialog.findViewById(R.id.txt_id_transaksi);
        txt_nominal=mydialog.findViewById(R.id.txt_nominal);
        gson=new GsonBuilder().create();

        final Intent intent=getIntent();
        Currentpoin=helper.meisinteger(helper.prefs.getString("curr_point","0"));
        txt_4.setText("Poin anda "+helper.prefs.getString("curr_point","0"));
        WebView webView = findViewById(R.id.webview_1);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = voucherTukar.deskripsi_voucher;


        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        builder1.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if(helper.meisinteger(voucherTukar.besaran_voucher)<=Currentpoin) {

                    action_tukarpoin();
                }
                else{
                    AlertDialog.Builder builder33 = new AlertDialog.Builder(DetailVouchertukarActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
                    builder33.setMessage("Total poin anda belum mencukupi untuk menukarkan voucher ini "+helper.prefs.getString("curr_point","0"))
                            .setTitle("Poin tidak cukup");
                    builder33.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                    AlertDialog dialog33 = builder33.create();
                    dialog33.show();
                }
            }
        });
        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        dialog1 = builder1.create();
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

        }






    }
    public void tukarpoin(View view){
        txt_id.setText(voucherTukar.nama_voucher);
        txt_nominal.setText(voucherTukar.besaran_voucher);
        //builder.setView(mydialog);
        // builder.setMessage("Anda Yakin Ingin Menambahkan Transaksi "+edt_id_transaksi.getText().toString()+" Sebesar "+helper.torupiah(currpoint));



        dialog1.show();

    }
    public void action_tukarpoin(){
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    String firebase_token= FirebaseInstanceId.getInstance().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);
                    params.put("ftoken", firebase_token);
                    params.put("id_voucher",voucherTukar.id_voucher);
                    params.put("jumlah","1");
                    service_connector.sendpostrequest(DetailVouchertukarActivity.this, "tukarvoucher", params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                VoucherPelanggan voucherTukar=gson.fromJson(jsonObject.getString("data"), VoucherPelanggan.class);



                                Intent intent=new Intent(DetailVouchertukarActivity.this,DetailVoucherActivity.class);
                                intent.putExtra("voucher",voucherTukar);
                                startActivity(intent);

                            }
                            catch (JSONException jo){

                            }

                        }

                        @Override
                        public void onNoConnection(String message) {

                        }

                        @Override
                        public void OnServerError(String message) {

                        }

                        @Override
                        public void OnTimeOut() {

                        }
                    });
                } else {
                    // Handle error -> task.getException();
                    Log.i("ez", task.getException().getMessage());
                }

            }
        });
    }
}
