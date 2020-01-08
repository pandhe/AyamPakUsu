package com.pontianak.ayampakusu;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pontianak.ayampakusu.adapter.NotifikasiAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotifikasiActivity extends AppCompatActivity {
    RecyclerView recy_notif;
    NotifikasiAdapter notifikasiAdapter;
    List<Notifikasi>arrayofnotif=new ArrayList<>();
    SqliteHelper sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        sqliteHelper=new SqliteHelper(this);
        recy_notif=findViewById(R.id.recy_notif);
        recy_notif.setLayoutManager(new LinearLayoutManager(this));
        notifikasiAdapter=new NotifikasiAdapter(arrayofnotif, this, new NotifikasiAdapter.onSelect() {
            @Override
            public void onselectitem(int item) {

            }

            @Override
            public void onDelete(int item) {
                Notifikasi notifikasi=arrayofnotif.get(item);
                sqliteHelper.delete(String.valueOf(notifikasi.id));
                arrayofnotif.clear();
                arrayofnotif.addAll(sqliteHelper.getAllNotes());
                notifikasiAdapter.notifyDataSetChanged();

            }
        });
        recy_notif.setAdapter(notifikasiAdapter);
        arrayofnotif.clear();
        arrayofnotif.addAll(sqliteHelper.getAllNotes());
        notifikasiAdapter.notifyDataSetChanged();


    }
}
