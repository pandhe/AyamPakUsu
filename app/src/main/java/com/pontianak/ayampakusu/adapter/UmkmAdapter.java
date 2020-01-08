package com.pontianak.ayampakusu.adapter;

import android.content.Context;
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
import com.pontianak.ayampakusu.R;

import java.util.List;

public class UmkmAdapter extends RecyclerView.Adapter<UmkmAdapter.praktekholder> {
    public List<Umkm> umkm;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();
    private AdapterRequest ar;

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_umkm;
        TextView txt_produk_umkm;


        public praktekholder(View itemView) {
            super(itemView);
            this.img_umkm = itemView.findViewById(R.id.img_umkm);
            this.txt_produk_umkm=itemView.findViewById(R.id.txt_produk_umkm);



        }
    }

    @Override
    public int getItemCount() {
        return this.umkm.size();
    }

    public UmkmAdapter(List<Umkm> umkm, Context ctx) {
        this.umkm = umkm;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
        try{
            this.ar=((AdapterRequest)ctx);
        }
        catch (ClassCastException cce){

        }
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_umkm, parent, false);

        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, final int position) {
        final Umkm praktek = umkm.get(position);

       Glide.with(ctx).load(myConfig.main_url + "images/umkm/" + praktek.foto_produk).into(holder.img_umkm);
       holder.txt_produk_umkm.setText(praktek.nama_produk);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ar.onOpenItem(position);
           }
       });


    }
    public static interface AdapterRequest{
        void onOpenItem(int position);
    }
}
