package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pontianak.ayampakusu.DetailFood;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

public class ListMoreItemAdapter extends RecyclerView.Adapter<ListMoreItemAdapter.praktekholder> {
    public List<ItemMore> itemMores;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();
    //Glide glide;

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
        return this.itemMores.size();
    }

    public ListMoreItemAdapter(List<ItemMore> itemMores, Context ctx) {
        this.itemMores = itemMores;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
        //this.glide=Glide.with(ctx);
    }

    @NonNull
    @Override
    public ListMoreItemAdapter.praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_moreitem, parent, false);

        return new ListMoreItemAdapter.praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMoreItemAdapter.praktekholder holder, final int position) {
        final ItemMore praktek = itemMores.get(position);
        switch (praktek.s_item){

            case 1:
                Glide.with(ctx).load(myConfig.main_url + "images/promo/" + praktek.gambar_item).into(holder.img_umkm);

                break;
            case 2:
                Glide.with(ctx).load(myConfig.main_url + "images/umkm/" + praktek.gambar_item).into(holder.img_umkm);

                break;
            case 0:
            case 3:
            case 4:
                Glide.with(ctx).load(myConfig.main_url + "images/tumb_food/" + praktek.gambar_item).into(holder.img_umkm);
                break;

        }

       holder.txt_produk_umkm.setText(praktek.nama_item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (praktek.s_item){
                    case 0:
                    case 3:
                    case 4:
                        Intent intent2=new Intent(ctx, DetailFood.class);
                        intent2.putExtra("url",myConfig.main_url+"banner/food/"+itemMores.get(position).getId_item());
                        intent2.putExtra("nama",praktek.nama_item);
                        intent2.putExtra("foto",praktek.gambar_item);
                        intent2.putExtra("harga",praktek.harga_item);

                        Log.i("ez",praktek.id_item);
                        ctx.startActivity(intent2);
                        break;

                    case 1:
                        Intent intent=new Intent(ctx, WebActivity.class);
                        intent.putExtra("url",myConfig.main_url);
                        ctx.startActivity(intent);

                        break;
                    case 2:
                        //Glide.with(ctx).load(myConfig.main_url + "images/umkm/" + praktek.gambar_item).into(holder.img_umkm);

                        break;
                }
            }
        });



    }
}