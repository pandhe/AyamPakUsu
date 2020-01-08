package com.pontianak.ayampakusu;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;


import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pontianak.ayampakusu.adapter.ItemMore;
import com.pontianak.ayampakusu.adapter.ListMoreItemAdapter;
import com.pontianak.ayampakusu.adapter.Voucher;
import com.pontianak.ayampakusu.adapter.VoucherAdapter;
import com.pontianak.ayampakusu.adapter.VoucherPelanggan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class VoucherActivity extends AppCompatActivity implements VoucherAdapter.AdapterInterface {
    RecyclerView recy_item;
    RecyclerView.LayoutManager mlm1;
    SwipeRefreshLayout swipeRefreshLayout;
    VoucherAdapter listMoreItemAdapter;
    List<VoucherPelanggan> arrayOfItem=new ArrayList<>();
    Service_Connector service_connector;
    TextView mTitle;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    MyConfig myConfig=new MyConfig();
    Gson gson;
   // SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onselected(int position) {
        VoucherPelanggan praktek = arrayOfItem.get(position);
        Intent intent=new Intent(VoucherActivity.this, DetailVoucherActivity.class);
        intent.putExtra("voucher",praktek);

        startActivityForResult(intent,myConfig.REQUEST_PAY_WITH_VOUCHER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.txtbar);
        mTitle.setText("Voucher Anda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        recy_item=findViewById(R.id.list);
        recy_item.setNestedScrollingEnabled(true);
        mlm1=new LinearLayoutManager(this);
        service_connector=new Service_Connector();
        listMoreItemAdapter=new VoucherAdapter(arrayOfItem,this);
        recy_item.setLayoutManager(mlm1);
        recy_item.setAdapter(listMoreItemAdapter);
        mAuth = FirebaseAuth.getInstance();
        gson= new GsonBuilder().create();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadvoucher();
            }
        });


        currentUser = mAuth.getCurrentUser();
        loadvoucher();


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==myConfig.REQUEST_PAY_WITH_VOUCHER){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                intent.putExtras(data);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void loadvoucher(){
        swipeRefreshLayout.setRefreshing(true);
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);
                    service_connector.sendpostrequest(VoucherActivity.this, "getmyvoucher", params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onResponese(String response) {
                            arrayOfItem.clear();
                            listMoreItemAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            try{
                                JSONObject respon=new JSONObject(response);
                                if(respon.getString("status").equals("1")){
                                    JSONArray ja=new JSONArray(respon.getString("data"));
                                    for(int i=0;i<ja.length();i++){
                                        JSONObject voucher=new JSONObject(ja.get(i).toString());
                                        VoucherPelanggan voucherPelanggan=gson.fromJson(ja.get(i).toString(),VoucherPelanggan.class);
                                        //Voucher vcr=new Voucher(voucher.getString("id_voucher_pelanggan"),voucher.getString("nama_voucher"),voucher.getString("besaran_voucher"),"",voucher.getString("datetime_stop_voucher"),"",voucher.getString("image_voucher"));
                                        arrayOfItem.add(voucherPelanggan);

                                    }
                                    listMoreItemAdapter.notifyDataSetChanged();

                                    //setResult(RESULT_OK, intent);
                                    //finish();
                                }
                            }
                            catch (JSONException joe){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onNoConnection(String message) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void OnServerError(String message) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void OnTimeOut() {
                            swipeRefreshLayout.setRefreshing(false);
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
