package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.Notifikasi;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.praktekholder> {
    public List<Notifikasi> promo;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();
    onSelect onselect;

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_slider;
        TextView txt_nama_promo,txt_keterangan_promo,txt_hapus;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.img_slider = itemView.findViewById(R.id.img_promo);
            this.txt_nama_promo=itemView.findViewById(R.id.txt_nama_promo);
            this.guideline13=itemView.findViewById(R.id.guideline27);
            this.txt_keterangan_promo=itemView.findViewById(R.id.txt_keterangan_promo);
            this.txt_hapus=itemView.findViewById(R.id.txt_hapus);


        }
    }
    public interface onSelect{
        public void onselectitem(int item);
        public void onDelete(int item);
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public NotifikasiAdapter(List<Notifikasi> promo, Context ctx,onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
        this.onselect=onselect;
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_notif, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, final int position) {
        final Notifikasi praktek = promo.get(position);





        //Glide.with(ctx).load(myConfig.main_url + "images/promo/" + praktek.gambar_promo).into(holder.img_slider);
        holder.txt_nama_promo.setText(praktek.getTitle_notif());
        holder.txt_keterangan_promo.setText(praktek.getContent_notif());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onselect.onselectitem(position);
               // Intent intent=new Intent(ctx, WebActivity.class);
                //intent.putExtra("url",myConfig.main_url+"banner/page/"+praktek.id_promo);
                //ctx.startActivity(intent);
            }
        });
        holder.txt_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onselect.onDelete(position);
            }
        });

    }
}
