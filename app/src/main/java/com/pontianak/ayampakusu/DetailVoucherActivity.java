package com.pontianak.ayampakusu;

import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.ayampakusu.adapter.KodeVoucherAdapter;
import com.pontianak.ayampakusu.adapter.Kode_voucher;
import com.pontianak.ayampakusu.adapter.VoucherPelanggan;
import com.pontianak.ayampakusu.adapter.VoucherTukar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailVoucherActivity extends AppCompatActivity {
    private MyConfig myConfig;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    VoucherPelanggan voucherPelanggan;
    Service_Connector service_connector;
    RecyclerView recyclerView;
    List<Kode_voucher> kode_vouchers=new ArrayList<>();
    KodeVoucherAdapter kodeVoucherAdapter;
    Gson gson;
    Group group2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voucher);
        myConfig=new MyConfig();
        mAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recy_1);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        kodeVoucherAdapter=new KodeVoucherAdapter(kode_vouchers,this);
        recyclerView.setAdapter(kodeVoucherAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        gson=new GsonBuilder().create();
        group2=findViewById(R.id.group2);

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

        }
        Button bt_reedem=findViewById(R.id.bt_reedem);
        ImageView img_content=findViewById(R.id.img_content);
        TextView txt_nama_voucher=findViewById(R.id.txt_keterangan_voucher);
        TextView txt_masa_berlaku=findViewById(R.id.txt_tanggal_berlaku);
        Intent intent=getIntent();
        voucherPelanggan=intent.getParcelableExtra("voucher");
        Glide.with(this).load(myConfig.main_url + "images/voucher/" + voucherPelanggan.image_voucher).into(img_content);
        txt_nama_voucher.setText(voucherPelanggan.nama_voucher);
        txt_masa_berlaku.setText(voucherPelanggan.datetime_mulai_berlaku_voucher);
        if(voucherPelanggan.status_voucher.equals("KUPON")){
            bt_reedem.setVisibility(View.GONE);
        }
        else{
            group2.setVisibility(View.GONE);
        }

        bt_reedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inteen;
                inteen=new Intent(DetailVoucherActivity.this, QrCodeActivity.class);
                inteen.putExtra("id_voucher",voucherPelanggan.id_voucher_pelanggan);
                startActivityForResult(inteen,myConfig.REQUEST_QR_CODE);
            }
        });
        getkodevoucher();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==myConfig.REQUEST_QR_CODE){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                intent.putExtras(data);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    public void getkodevoucher(){
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    String firebase_token= FirebaseInstanceId.getInstance().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);
                    params.put("ftoken", firebase_token);
                    params.put("id_voucher",voucherPelanggan.id_voucher_pelanggan);
                    service_connector.sendpostrequest(DetailVoucherActivity.this, "getkodevoucher", params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            kode_vouchers.clear();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray ja=new JSONArray(jsonObject.getString("data"));
                               kodeVoucherAdapter.notifyDataSetChanged();

                                for(int i=0;i<ja.length();i++){
                                   Kode_voucher voucherTukar=gson.fromJson(ja.get(i).toString(),Kode_voucher.class);

                                    //Promo mypromo=new Promo(mymenus.getString("id_voucher"),mymenus.getString("nama_voucher"),mymenus.getString("image_voucher"),"0");
                                    kode_vouchers.add(voucherTukar);

                                }
                                kodeVoucherAdapter.notifyDataSetChanged();


                            }
                            catch (JSONException jo){
                                Log.i("ez",jo.getMessage());

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
