package com.pontianak.ayampakusu;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pontianak.ayampakusu.adapter.ItemMore;
import com.pontianak.ayampakusu.adapter.ListMoreItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MoreActivity extends AppCompatActivity {
RecyclerView recy_item;
RecyclerView.LayoutManager mlm1;
SwipeRefreshLayout swipeRefreshLayout;
ListMoreItemAdapter listMoreItemAdapter;
List<ItemMore>arrayOfItem=new ArrayList<>();
Service_Connector service_connector;
ProgressBar progressmore;
TextView txt_loading;
    int s_item=0;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.txtbar);
        mTitle.setText("Daftar menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        recy_item=findViewById(R.id.list);
        recy_item.setNestedScrollingEnabled(false);
        mlm1=new LinearLayoutManager(this);
        service_connector=new Service_Connector();
        listMoreItemAdapter=new ListMoreItemAdapter(arrayOfItem,this);
        recy_item.setLayoutManager(mlm1);
        recy_item.setAdapter(listMoreItemAdapter);
        txt_loading=findViewById(R.id.txt_loading);
        progressmore=findViewById(R.id.progress_more);
        Intent intent=getIntent();

        s_item=intent.getIntExtra("s_item",0);
        getitem(s_item);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("ez","refresh");
                getitem(s_item);
            }
        });






    }
     private void setprogress(Boolean load,String message){
        if(load)
            progressmore.setVisibility(View.VISIBLE);
        else
            progressmore.setVisibility(View.INVISIBLE);

        if(message.length()>3){
            txt_loading.setVisibility(View.VISIBLE);
            txt_loading.setText(message);
        }

        else
            txt_loading.setVisibility(View.INVISIBLE);

        swipeRefreshLayout.setRefreshing(false);
     }
    public void getitem(final int s_item){
        setprogress(true,"Mohon Tunggu...");
        String target="";
        switch (s_item){
            case 0:
                target="getmenufavorit";
                mTitle.setText("Menu Favorit");
                break;

            case 1:
                target="getfullpromo";
                mTitle.setText("Promo");
                break;
            case 2:
                target="getfullumkm";
                mTitle.setText("UMKM");
                break;
            case 3:
                target="getmenubaru";
                mTitle.setText("Menu Terbaru");
                break;
            case 4:
                Intent intent=getIntent();

                target="getmenu/"+intent.getStringExtra("s_kategori");
                mTitle.setText(intent.getStringExtra("s_title"));
                break;
        }
        Log.i("ez",target);
        service_connector.sendgetrequest(this, target, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {
                setprogress(false,message);

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject mydata=new JSONObject(response);
                    JSONArray ja=new JSONArray(mydata.getString("data"));
                    int countdata=0;

                    switch (s_item){


                        case 1:
                            arrayOfItem.clear();
                            listMoreItemAdapter.notifyDataSetChanged();
                            if(ja.length()>0)

                            for(int i=0;i<ja.length();i++){
                                JSONObject items=new JSONObject(ja.get(i).toString());

                                ItemMore im=new ItemMore(items.getString("id_promo"),items.getString("nama_promo"),"0"," ",items.getString("gambar_promo"),s_item);
                                arrayOfItem.add(im);
                                countdata++;
                            }
                            listMoreItemAdapter.notifyDataSetChanged();

                            break;
                        case 2:
                            arrayOfItem.clear();
                            listMoreItemAdapter.notifyDataSetChanged();
                             for(int i=0;i<ja.length();i++){
                                JSONObject items=new JSONObject(ja.get(i).toString());

                                ItemMore im=new ItemMore(items.getString("id_produk"),items.getString("nama_produk"),items.getString("harga_produk")," ",items.getString("foto_produk"),s_item);
                                arrayOfItem.add(im);
                                 countdata++;
                            }
                            listMoreItemAdapter.notifyDataSetChanged();

                            break;
                        case 0:
                        case 3:
                        case 4:
                            arrayOfItem.clear();
                            listMoreItemAdapter.notifyDataSetChanged();
                            for(int i=0;i<ja.length();i++){
                                JSONObject items=new JSONObject(ja.get(i).toString());

                                ItemMore im=new ItemMore(items.getString("id_food"),items.getString("nama_food"),items.getString("harga_food"),items.getString("deskripsi_food"),items.getString("foto_food"),s_item);
                                arrayOfItem.add(im);
                                countdata++;
                            }
                            listMoreItemAdapter.notifyDataSetChanged();
                            break;
                    }

                    setprogress(false,"");
                    if(countdata==0){
                        setprogress(false,"Tidak Ada Data Yang Dapat Ditampilkan");
                    }
                }
                catch (JSONException JO){
                    setprogress(false,JO.getMessage());
                    Log.i("ez",JO.getMessage());
                }

            }

            @Override
            public void onNoConnection(String message) {
                setprogress(false,message);

            }

            @Override
            public void OnServerError(String message) {
                setprogress(false,message);

            }

            @Override
            public void OnTimeOut() {

            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
