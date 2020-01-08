package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.QrCodeActivity;
import com.pontianak.ayampakusu.R;

import java.util.List;

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



        //GET,url+"?id_user="+shared+"&id_buku="+praktek.id

    }
}
