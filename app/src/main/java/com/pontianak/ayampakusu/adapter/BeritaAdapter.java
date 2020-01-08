package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.QrCodeActivity;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.praktekholder> {
    public List<Promo> promo;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_slider;
        TextView txt_nama_promo,txt_keterangan_promo;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.img_slider = itemView.findViewById(R.id.img_promo);
            this.txt_nama_promo=itemView.findViewById(R.id.txt_nama_promo);
            this.guideline13=itemView.findViewById(R.id.guideline27);
            this.txt_keterangan_promo=itemView.findViewById(R.id.txt_keterangan_promo);


        }
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public BeritaAdapter(List<Promo> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_berita, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final Promo praktek = promo.get(position);




        Glide.with(ctx).load(myConfig.main_url + "images/promo/" + praktek.gambar_promo).into(holder.img_slider);
        holder.txt_nama_promo.setText(praktek.nama_promo);
        holder.txt_keterangan_promo.setText(praktek.subtitle_promo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, WebActivity.class);
                intent.putExtra("gambar",myConfig.main_url + "images/promo/" + praktek.gambar_promo);
                intent.putExtra("judul",praktek.nama_promo);
                intent.putExtra("url",myConfig.main_url+"banner/page/"+praktek.id_promo);
                ctx.startActivity(intent);
            }
        });


    }
}
