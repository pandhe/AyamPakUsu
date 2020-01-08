package com.pontianak.ayampakusu;


import android.os.Bundle;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private MapHelper mapHelper;
    private Helper helper;
    Service_Connector service_connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapHelper=new MapHelper(this,savedInstanceState,R.id.mapView);
        helper=new Helper(this);
        service_connector=new Service_Connector();
        service_connector.sendgetrequest(this, "getfullpakusu", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    JSONArray pakusu=new JSONArray(respon.getString("data"));
                    for(int i=0;i<pakusu.length();i++){
                        JSONObject jo=new JSONObject(pakusu.get(i).toString());

                        LatLng sydney = new LatLng(helper.meisdouble(jo.getString("lat_pakusu")),helper.meisdouble(jo.getString("lng_pakusu")));
                        mapHelper.googleMap.addMarker(new MarkerOptions().position(sydney).title(jo.getString("nama_pakusu")));
                    }


                }
                catch (Exception e){

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



}
