package com.pontianak.ayampakusu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
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
import com.pontianak.ayampakusu.adapter.BeritaAdapter;
import com.pontianak.ayampakusu.adapter.Mymenu;
import com.pontianak.ayampakusu.adapter.Promo;
import com.pontianak.ayampakusu.adapter.PromoAdapter;
import com.pontianak.ayampakusu.adapter.PromoPagerAdapter;
import com.pontianak.ayampakusu.adapter.Umkm;
import com.pontianak.ayampakusu.adapter.UmkmAdapter;
import com.pontianak.ayampakusu.adapter.Voucher;
import com.pontianak.ayampakusu.adapter.VoucherAdapter;
import com.pontianak.ayampakusu.adapter.VoucherTukar;
import com.pontianak.ayampakusu.adapter.VoucherTukarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements UmkmAdapter.AdapterRequest {
    RecyclerView recy_menu,recy_fitur,recy_umkm,recy_berita;
    RecyclerView.LayoutManager mlm,mlm2,mlm3,mlm4,mlm5;
    ConstraintLayout lay_progress, lay_login;
    ViewPager vf_promo;
    RecyclerView recy_promo;
    Button bt_login, bt_daftar;
    TextView txt_akun;
    ImageView img_profil;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    int REQUEST_MEMBER = 1;
    Helper helper;
    MyMenuAdapter menuAdapter,fitrAdapter;
    BeritaAdapter beritaAdapter;
    UmkmAdapter umkmAdapter;
    List<Mymenu> arrayofmenu = new ArrayList<>();
    final ArrayList<String>images=new ArrayList<>();
    List<Mymenu> arrayoffitur=new ArrayList<>();
    List<Promo>promos=new ArrayList<>();
    List<VoucherTukar>voucherTukars=new ArrayList<>();
    List<Umkm>arrayofumkm=new ArrayList<>();
    List<Promo>arrayofberita=new ArrayList<>();
    Service_Connector service_connector;
    int REQUEST_PERMISSION=241;
    int REQUEST_QR_CODE=1007;
    int REQUEST_CHANGE_PROFIL=1008;
    private PromoAdapter promoAdapter;
    private VoucherTukarAdapter voucherTukarAdapter;
    ImageButton bt_vocer_1,bt_vocer_2,bt_vocer_3,bt_more_promo,bt_more_umkm;
    Button bt_point_cashback,bt_stiker,bt_sticker_count,bt_sticker_ticker;
    String point;
    private String id_token;
    private MyConfig myConfig=new MyConfig();
    private Group group1,group2,group45,group_promo;
    private String curr_point="0";
    LayoutInflater gabung2;
    View mydialog ;
    ImageView img_detail_makanan;
   AlertDialog nad;
    TextView txt_harga_dialog;
    public BroadcastReceiver penerima;
    SwipeRefreshLayout swiperefresh;
    SqliteHelper sqliteHelper;
    TextView txt_count_notif;
    int count_notif;
    ImageButton bt_lonceng;
    TextView txt_logindulu;
    Gson gson;
    boolean isregister=true;

    private View.OnClickListener klikvoucher=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,VoucherActivity.class);
            intent.putExtra("action",2);
            startActivityForResult(intent,myConfig.REQUEST_VOUCHER);
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            sqliteHelper.insertNote(intent.getStringExtra("title"),intent.getStringExtra("body"),"","","");
            loadpoint(false);
            //playbeep();
            txt_count_notif.setText(String.valueOf(sqliteHelper.getNotesCount()));



        }
    };

    private ViewPager.OnClickListener loginclick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentUser = mAuth.getCurrentUser();
            Intent intent ;
            if (currentUser != null) {
                intent = new Intent(MainActivity.this, ProfilActivity.class);
                intent.putExtra("s_item", 4);
                startActivity(intent);
            }
            else{
                intent = new Intent(MainActivity.this, SlideLoginActivity.class);
                intent.putExtra("s_item",2);
                startActivityForResult(intent, REQUEST_MEMBER);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        txt_akun = findViewById(R.id.txt_akun);
        sqliteHelper=new SqliteHelper(this);
        img_profil = findViewById(R.id.img_profil);
        service_connector = new Service_Connector();
        recy_menu = findViewById(R.id.recy_menu);
        recy_fitur=findViewById(R.id.recy_fitur);
        recy_promo=findViewById(R.id.recy_promo);
        recy_umkm=findViewById(R.id.recy_umkm);
        recy_berita=findViewById(R.id.recy_berita);
        gabung2 = getLayoutInflater();
        nad = new AlertDialog.Builder(this).create();
        mydialog = gabung2.inflate(R.layout.dialog_porsi, null);
        txt_harga_dialog=mydialog.findViewById(R.id.txt_dialog_harga);
        img_detail_makanan = (ImageView) mydialog.findViewById(R.id.img_detail_makanan);
        promoAdapter=new PromoAdapter(promos,this);
        voucherTukarAdapter=new VoucherTukarAdapter(voucherTukars, this, new VoucherTukarAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {

                Intent intent=new Intent(MainActivity.this, DetailVouchertukarActivity.class);
                Log.i("ez curr point ",curr_point);
                intent.putExtra("point",curr_point);

                intent.putExtra("item",voucherTukars.get(item));
                startActivity(intent);
            }
        });
        fitrAdapter=new MyMenuAdapter(arrayoffitur,this);
        umkmAdapter=new UmkmAdapter(arrayofumkm,this);
        beritaAdapter=new BeritaAdapter(arrayofberita,this);
        recy_umkm.setAdapter(umkmAdapter);
        mlm3=new GridLayoutManager(this,5);
        mlm4=new GridLayoutManager(this,2);
        mlm5=new LinearLayoutManager(this);
        recy_berita.setLayoutManager(mlm5);
        swiperefresh=findViewById(R.id.swiperefresh);
        txt_count_notif=findViewById(R.id.txt_count_notif);
        txt_logindulu=findViewById(R.id.txt_logindulu);
        gson= new GsonBuilder().create();




        group1=findViewById(R.id.group_1);
        group2=findViewById(R.id.group_2);
        group45=findViewById(R.id.group_45);
        group1.setVisibility(View.INVISIBLE);
        group_promo=findViewById(R.id.group_promo);





        bt_point_cashback=findViewById(R.id.bt_point_cashback);


        //bt_vocer_1=findViewById(R.id.bt_vocer_1);
       // bt_vocer_2=findViewById(R.id.bt_vocer_2);
       // bt_vocer_3=findViewById(R.id.bt_vocer_3);
        bt_point_cashback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,QrCodeActivity.class);
                intent.putExtra("point",curr_point);
                startActivityForResult(intent,REQUEST_QR_CODE);
            }
        });

        mlm2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        menuAdapter = new MyMenuAdapter(arrayofmenu, this);
        recy_promo.setAdapter(voucherTukarAdapter);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);

        } else {

        }
        mlm = new GridLayoutManager(this, 5);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(MyConfig.PUSH_NOTIFICATION));





        recy_menu.setLayoutManager(mlm);
        recy_promo.setLayoutManager(mlm2);
        recy_menu.setAdapter(menuAdapter);
        recy_fitur.setLayoutManager(mlm3);
        recy_fitur.setAdapter(fitrAdapter);
        recy_umkm.setLayoutManager(mlm4);
        recy_berita.setAdapter(beritaAdapter);
        recy_umkm.setNestedScrollingEnabled(false);
        recy_fitur.setNestedScrollingEnabled(false);
        recy_menu.setNestedScrollingEnabled(false);
        recy_promo.setNestedScrollingEnabled(false);
        bt_more_promo=findViewById(R.id.bt_more_promo);
        bt_more_umkm=findViewById(R.id.bt_more_umkm);
        bt_lonceng=findViewById(R.id.bt_lonceng);

        Glide.with(this).load(myConfig.main_url+"/images/ic_account_n.png").apply(RequestOptions.circleCropTransform()).into(img_profil);

        helper = new Helper(this);
        txt_akun.setText(helper.prefs.getString("email", getString(R.string.masuk_daftar)));


        bt_more_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (currentUser != null) {

                    intent=new Intent(MainActivity.this,VoucherActivity.class);
                    intent.putExtra("s_item",1);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(MainActivity.this, SlideLoginActivity.class);
                    //intent.putExtra("s_item", 4);
                    startActivity(intent);

                }


            }
        });
        bt_more_umkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MoreActivity.class);
                intent.putExtra("s_item",2);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            group1.setVisibility(View.VISIBLE);
            group2.setVisibility(View.INVISIBLE);
            loadpoint(true);


        } else {
            Log.i("ez", "kosong");
            getmenu();
        }
        txt_akun.setOnClickListener(loginclick);

        txt_logindulu.setOnClickListener(loginclick);

        img_profil.setOnClickListener(loginclick);
        //loadpoint();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentUser != null) {
                    group1.setVisibility(View.VISIBLE);
                    group2.setVisibility(View.INVISIBLE);
                    loadpoint(true);


                } else {
                    Log.i("ez", "kosong");
                    getmenu();
                }
            }
        });

        bt_lonceng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(MainActivity.this,NotifikasiActivity.class);
                startActivityForResult(inte,myConfig.REQUEST_NOTIFICATION);
            }
        });
        count_notif=sqliteHelper.getNotesCount();
        if(count_notif==0){
            txt_count_notif.setVisibility(View.GONE);
        }
        else{
            txt_count_notif.setVisibility(View.VISIBLE);
        }


        txt_count_notif.setText(String.valueOf(sqliteHelper.getNotesCount()));





        //setCount(this,"2",0);
       // setCount(this,"Point:18500","0");
      //  setCount1(this,"Stiker","2","2");
       // setCount2(this,"Voucher","4");


    }







    @Override
    public void onOpenItem(int position) {
        final Umkm makanan = arrayofumkm.get(position);
        nad.setTitle(makanan.nama_produk);
        txt_harga_dialog.setText(makanan.harga_produk);
        Glide.with(this).load(myConfig.main_url + "images/umkm/" + makanan.foto_produk).into(img_detail_makanan);

        Intent intent2=new Intent(MainActivity.this, DetailUmkm.class);
        intent2.putExtra("url",myConfig.main_url+"banner/umkm/"+makanan.id_produk);
        intent2.putExtra("nama",makanan.nama_produk);
        intent2.putExtra("foto",makanan.foto_produk);
        intent2.putExtra("harga",makanan.harga_produk);

        startActivity(intent2);








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("tes",String.valueOf(requestCode));

        if (requestCode == REQUEST_MEMBER) {
            if (resultCode == RESULT_OK) {
                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    txt_akun.setText(currentUser.getEmail());
                    group1.setVisibility(View.VISIBLE);
                    group2.setVisibility(View.INVISIBLE);
                    loadpoint(false);

                        loginbackend(currentUser);

                }
            } else {

            }

        }
        if(requestCode==myConfig.REQUEST_CHANGE_PROFILE){
            if(resultCode==RESULT_OK){
                loadpoint(false);
            }
        }


         if(requestCode==REQUEST_QR_CODE || requestCode==myConfig.REQUEST_VOUCHER){
            if(resultCode==RESULT_OK){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                String message="";
                message+=getString(R.string.dapat_voucher);
                message+=System.getProperty("line.separator");
                if(helper.meisinteger(data.getStringExtra("voucher"))>0){
                    message+=String.format(getResources().getString(R.string.voucher_sebesar),helper.torupiah(helper.meisinteger(data.getStringExtra("voucher"))));
                    message+=System.getProperty("line.separator");
                }
                if(!data.getStringExtra("add_stiker").equals("0")){
                    message+=String.format(getResources().getString(R.string.tambahan_stiker),helper.torupiah(helper.meisinteger(data.getStringExtra("add_stiker"))));

                }
                builder1.setMessage(message);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Oke",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();
                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    loadpoint(false);
                }
                else{

                }
            }


            }
        if(requestCode==myConfig.REQUEST_NOTIFICATION){

                txt_count_notif.setText(String.valueOf(sqliteHelper.getNotesCount()));

        }




        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadpoint(final Boolean loadmenu){
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    String firebase_token= FirebaseInstanceId.getInstance().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);
                    params.put("ftoken", firebase_token);
                    service_connector.sendpostrequest(MainActivity.this, "loadpoint", params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject usr=new JSONObject(jsonObject.getString("user"));
                                if(usr.getString("photoUrl").equals("null")){
                                    Glide.with(MainActivity.this).load(myConfig.main_url+"/images/ic_account_n.png").apply(RequestOptions.circleCropTransform()).into(img_profil);

                                }
                                else{
                                    Glide.with(MainActivity.this).load(usr.getString("photoUrl")).apply(RequestOptions.circleCropTransform()).into(img_profil);

                                }

                                bt_point_cashback.setText(jsonObject.getString("point")+" Poin");
                                curr_point=jsonObject.getString("point");
                                helper.meditor.putString("curr_point", jsonObject.getString("point")).apply();
                                txt_akun.setText(jsonObject.getString("nama_user"));
                                isregister=true;

                                if(jsonObject.getString("register").equals("0")){
                                    isregister=false;
                                    if(loadmenu){
                                        getmenu();
                                    }
                                   Intent intent=new Intent(MainActivity.this,ProfilActivity.class);
                                    startActivityForResult(intent,myConfig.REQUEST_CHANGE_PROFILE);

                                }
                                else{
                                    if(loadmenu){
                                        getmenu();
                                    }
                                }





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

    private void loginbackend(FirebaseUser user) {
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Map<String, String> mymap = new HashMap<>();
                    mymap.put("idtoken", idToken);
                    id_token=idToken;

                    service_connector.sendpostrequest(MainActivity.this, "login", mymap, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("login").equals("1")) {
                                    helper.meditor.putBoolean("islogin", true);
                                    setCount(MainActivity.this,jsonObject.getString("point"),"0");
                                } else {

                                }
                            } catch (Throwable t) {

                            }

                        }

                        @Override
                        public void onNoConnection(String message) {

                        }

                        @Override
                        public void OnServerError(String message) {
                            Log.i("ez", message);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_akun, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                mAuth.getInstance().signOut();
                helper.meditor.putBoolean("false", true);
                helper.meditor.putString("email", "Login atau Daftar").apply();
                txt_akun.setText("Login atau Daftar");
                group1.setVisibility(View.INVISIBLE);
                group2.setVisibility(View.VISIBLE);
                Glide.with(this).load(myConfig.main_url+"/images/ic_account_n.png").apply(RequestOptions.circleCropTransform()).into(img_profil);
                currentUser = mAuth.getCurrentUser();


                break;
            case R.id.action_syarat:
                Intent po=new Intent(this,SyaratKetentuanActivity.class);
                startActivity(po);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void getmenu() {
        service_connector.sendgetrequest(this, "getmenumakanan", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));
                    arrayofmenu.clear();
                    menuAdapter.notifyDataSetChanged();
                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());
                        Mymenu mymenu = new Mymenu(mymenus.getInt("action"), mymenus.getString("image_menu"), mymenus.getString("title_menu"),mymenus.getString("id_menu"));
                        arrayofmenu.add(mymenu);

                    }
                    menuAdapter.notifyDataSetChanged();
                    getmenufitur();
                }
                catch (JSONException JE){

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











    }
    private void getmenufitur(){
        service_connector.sendgetrequest(this, "getmenufitur", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    arrayoffitur.clear();
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));

                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());
                        Mymenu mymenu = new Mymenu(mymenus.getInt("action"), mymenus.getString("image_menu"), mymenus.getString("title_menu"));
                        arrayoffitur.add(mymenu);

                    }
                    fitrAdapter.notifyDataSetChanged();
                   getvouchertukar();
                }
                catch (JSONException JE){

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
    }

    private void getvouchertukar(){
        service_connector.sendgetrequest(this, "getvouchertukar", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));
                    voucherTukars.clear();
                    voucherTukarAdapter.notifyDataSetChanged();
                    if(ja.length()<1){
                        group_promo.setVisibility(View.GONE);
                    }
                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());
                        VoucherTukar voucherTukar=gson.fromJson(ja.get(i).toString(),VoucherTukar.class);

                        //Promo mypromo=new Promo(mymenus.getString("id_voucher"),mymenus.getString("nama_voucher"),mymenus.getString("image_voucher"),"0");
                        voucherTukars.add(voucherTukar);

                    }
                    voucherTukarAdapter.notifyDataSetChanged();
                    getUmkm();
                }
                catch (Exception JE){
                    Log.i("ez",JE.getMessage());

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

    }
    private void getpromo(){
        service_connector.sendgetrequest(this, "getalliklanpromo2", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));
                    promos.clear();
                    promoAdapter.notifyDataSetChanged();
                    if(ja.length()<1){
                        group_promo.setVisibility(View.GONE);
                    }
                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());

                        Promo mypromo=new Promo(mymenus.getString("id_promo"),mymenus.getString("nama_promo"),mymenus.getString("gambar_promo"),"0");
                        promos.add(mypromo);

                    }
                    promoAdapter.notifyDataSetChanged();
                    getUmkm();
                }
                catch (Exception JE){
                    Log.i("ez",JE.getMessage());

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

    }
    public void getUmkm(){
        service_connector.sendgetrequest(this, "getumkm", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));
                    arrayofumkm.clear();
                    umkmAdapter.notifyDataSetChanged();
                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());

                        Umkm mypromo=new Umkm(mymenus.getString("id_produk"),mymenus.getString("nama_produk"),mymenus.getString("foto_produk"));
                        arrayofumkm.add(mypromo);

                    }
                    umkmAdapter.notifyDataSetChanged();
                    group45.setVisibility(View.VISIBLE);
                    getberita();


                }
                catch (JSONException JE){

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
    }
    private void getberita(){
        service_connector.sendgetrequest(this, "getalliklanberita", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject res=new JSONObject(response);
                    JSONArray ja=new JSONArray(res.getString("data"));
                    arrayofberita.clear();
                    beritaAdapter.notifyDataSetChanged();
                    for(int i=0;i<ja.length();i++){
                        JSONObject mymenus=new JSONObject(ja.get(i).toString());

                        Promo mypromo=new Promo(mymenus.getString("id_promo"),mymenus.getString("nama_promo"),mymenus.getString("gambar_promo"),mymenus.getString("url_action"),mymenus.getString("subtitle_promo"));
                        arrayofberita.add(mypromo);

                    }
                    beritaAdapter.notifyDataSetChanged();
                    swiperefresh.setRefreshing(false);

                }
                catch (JSONException JE){

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

    }
    public void setCount(Context context, String nominal,String count) {


        LayerDrawable icon = (LayerDrawable) bt_vocer_1.getDrawable();

        CountDrawable badge;
        CountDrawable card;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        Drawable nom=icon.findDrawableByLayerId(R.id.ic_group_nominal);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context,0);
        }
        if (nom != null && reuse instanceof CountDrawable) {
            card = (CountDrawable) nom;
        } else {
            card = new CountDrawable(context,1);
        }

        badge.setCount(count);
        card.setCount(nominal);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        icon.setDrawableByLayerId(R.id.ic_group_nominal, card);
    }
    public void setCount1(Context context, String nominal,String count, String count2) {


        LayerDrawable icon = (LayerDrawable) bt_vocer_2.getDrawable();

        CountDrawable2 badge;
        CountDrawable2 card;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        Drawable nom=icon.findDrawableByLayerId(R.id.ic_group_nominal);
        if (reuse != null && reuse instanceof CountDrawable2) {
            badge = (CountDrawable2) reuse;
        } else {
            badge = new CountDrawable2(context,0);
        }
        if (nom != null && reuse instanceof CountDrawable) {
            card = (CountDrawable2) nom;
        } else {
            card = new CountDrawable2(context,1);
        }

        badge.setCount(count);
        card.setCount(nominal);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        icon.setDrawableByLayerId(R.id.ic_group_nominal, card);
    }
    public void setCount2(Context context, String nominal,String count) {


        LayerDrawable icon = (LayerDrawable) bt_vocer_3.getDrawable();

        CountDrawable badge;
        CountDrawable card;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        Drawable nom=icon.findDrawableByLayerId(R.id.ic_group_nominal);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context,0);
        }
        if (nom != null && reuse instanceof CountDrawable) {
            card = (CountDrawable) nom;
        } else {
            card = new CountDrawable(context,1);
        }

        badge.setCount(count);
        card.setCount(nominal);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        icon.setDrawableByLayerId(R.id.ic_group_nominal, card);
    }
    public class MyMenuAdapter extends RecyclerView.Adapter<MyMenuAdapter.praktekholder> {
        public List<Mymenu> listmenu;
        public Context ctx;
        public LayoutInflater lay;
        Helper helper;
        MyConfig myConfig=new MyConfig();
        public class praktekholder extends RecyclerView.ViewHolder{
            TextView title_menu;
            ImageView img_menu;

            public praktekholder(View itemView) {
                super(itemView);
                this.title_menu = itemView.findViewById(R.id.txt_menu);
                this.img_menu=itemView.findViewById(R.id.img_menu);

            }
        }

        @Override
        public int getItemCount() {
            return this.listmenu.size();
        }

        public MyMenuAdapter(List<Mymenu> listmenu, Context ctx) {
            this.listmenu = listmenu;
            this.ctx = ctx;
            this.lay=(LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            this.helper=new Helper(ctx);
        }

        @NonNull
        @Override
        public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview=lay.inflate(R.layout.content_list_mymenu,parent,false);
            return new praktekholder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull praktekholder holder, int position) {
            final Mymenu praktek=listmenu.get(position);
            holder.title_menu.setText(praktek.title);

            Glide.with(ctx).load(myConfig.main_url+"images/menu/"+praktek.image).apply(RequestOptions.circleCropTransform()).into(holder.img_menu);

            holder.title_menu.setText(praktek.title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Intent intent=new Intent(ctx,DetailMenu.class);
                intent.putExtra("id_praktek",praktek.id_praktek);
                ctx.startActivity(intent);*/
                Intent intent;
                    switch(praktek.action){
                        case 0:
                            intent=new Intent(MainActivity.this, MoreActivity.class);
                            intent.putExtra("s_item",4);
                            intent.putExtra("s_title",praktek.title);
                            intent.putExtra("s_kategori",praktek.id_menu);
                            startActivity(intent);
                            break;
                        case 1:
                            if (currentUser != null) {
                                if(isregister){
                                    intent = new Intent(MainActivity.this, QrCodeActivity.class);
                                    startActivityForResult(intent, REQUEST_QR_CODE);
                                }
                                else{
                                    intent=new Intent(MainActivity.this,ProfilActivity.class);
                                    startActivityForResult(intent,myConfig.REQUEST_CHANGE_PROFILE);
                                }


                            }
                            else {
                                intent = new Intent(MainActivity.this, SlideLoginActivity.class);
                                //intent.putExtra("s_item", 4);
                                startActivity(intent);

                            }

                            break;
                        case 2:
                            intent=new Intent(MainActivity.this, MoreActivity.class);
                            intent.putExtra("s_item",0);
                            startActivity(intent);
                            break;
                        case 3:

                                intent = new Intent(MainActivity.this, MoreActivity.class);
                                intent.putExtra("s_item", 3);
                                startActivity(intent);

                            break;
                        case 4:
                            if (currentUser != null) {
                                intent = new Intent(MainActivity.this, ProfilActivity.class);
                                intent.putExtra("s_item", 4);
                                startActivityForResult(intent,myConfig.REQUEST_CHANGE_PROFILE);
                            }
                            else{
                            intent = new Intent(MainActivity.this, SlideLoginActivity.class);
                            intent.putExtra("s_item",2);
                            startActivityForResult(intent, REQUEST_MEMBER);
                        }
                            break;
                        case 5:
                            intent=new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("s_item",3);
                            startActivity(intent);
                            break;
                    }

                }
            });
            //GET,url+"?id_user="+shared+"&id_buku="+praktek.id

        }
    }







}
