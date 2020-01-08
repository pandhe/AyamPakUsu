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
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.QrCodeActivity;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.praktekholder> {
    public List<Promo> promo;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_slider;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.img_slider = itemView.findViewById(R.id.img_promo);
            this.guideline13=itemView.findViewById(R.id.guideline13);


        }
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public PromoAdapter(List<Promo> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
    }

    @NonNull
    @Override
    public praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_promo, parent, false);
        return new praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull praktekholder holder, int position) {
        final Promo praktek = promo.get(position);
        if(position==0){
            holder.guideline13.setGuidelinePercent(0.1f);

        }

        Glide.with(ctx).load(myConfig.main_url + "images/promo/" + praktek.gambar_promo).into(holder.img_slider);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, WebActivity.class);
                intent.putExtra("url",myConfig.main_url+"banner/page/"+praktek.id_promo);
                ctx.startActivity(intent);
            }
        });


    }
}
