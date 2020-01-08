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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pontianak.ayampakusu.DetailVouchertukarActivity;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

public class VoucherTukarAdapter extends RecyclerView.Adapter<VoucherTukarAdapter.praktekholder> {
    public List<VoucherTukar> promo;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();
    onSelect onselect;

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_slider;
        Guideline guideline13;

        public praktekholder(View itemView) {
            super(itemView);
            this.img_slider = itemView.findViewById(R.id.img_promo);
            this.guideline13=itemView.findViewById(R.id.guideline13);


        }
    }
    public interface onSelect{
        public void onselectitem(int item);
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public VoucherTukarAdapter(List<VoucherTukar> promo, Context ctx, onSelect onselect) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
        this.onselect=onselect;
    }

    @NonNull
    @Override
    public VoucherTukarAdapter.praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_voucher_tukar, parent, false);
        return new VoucherTukarAdapter.praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherTukarAdapter.praktekholder holder, final int position) {
        final VoucherTukar praktek = promo.get(position);
        if(position==0){
            holder.guideline13.setGuidelinePercent(0.05f);

        }

        Glide.with(ctx).load(myConfig.main_url + "images/voucher/" + praktek.image_voucher).into(holder.img_slider);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onselect.onselectitem(position);

            }
        });


    }
}

