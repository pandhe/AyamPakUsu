package com.pontianak.ayampakusu;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHelper {
    int mapid;
    MapView mMapView;
    public GoogleMap googleMap;
    private Activity act;
    private Bundle bdl;
    Service_Connector service_connector;


    public MapHelper(Activity act, Bundle bdl, int mapid) {
        this.mapid = mapid;
        this.act=act;
        this.bdl=bdl;

        this.service_connector=new Service_Connector();

        mMapView = act.findViewById(mapid);
        mMapView.onCreate(bdl);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(act.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                try {
                    googleMap.setMyLocationEnabled(true);
                }
                catch (SecurityException se){

                }
                //keranjang card = new keranjang(themap.get("status_resto"), themap.get("nama_resto"), themap.get("item_order"), themap.get("harga_item"), themap.get("porsi_item"), themap.get("tarif"), themap.get("alamat_detail"),themap.get("namanohp"),themap.get("id_item"),themap.get("lat"),themap.get("lng"));
                //arrayofkeranjang.add(card);


                LocationListener mLocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        //start = new LatLng(location.getLatitude(), location.getLongitude());



                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };




                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-0.0442145,109.3296213);


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Ayam Pakusu"));
                //loadmarker();


            }
        });

    }
    public void setmarker(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
