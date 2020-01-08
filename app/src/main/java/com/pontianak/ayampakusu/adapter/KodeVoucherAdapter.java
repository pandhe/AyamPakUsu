package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
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

public class KodeVoucherAdapter extends RecyclerView.Adapter<KodeVoucherAdapter.praktekholder> {
    public List<Kode_voucher> promo;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();

    public class praktekholder extends RecyclerView.ViewHolder {
        TextView txt_1;

        public praktekholder(View itemView) {
            super(itemView);
            this.txt_1=itemView.findViewById(R.id.txt_1);
            //this.guideline13=itemView.findViewById(R.id.guideline13);


        }
    }

    @Override
    public int getItemCount() {
        return this.promo.size();
    }

    public KodeVoucherAdapter(List<Kode_voucher> promo, Context ctx) {
        this.promo = promo;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
    }

    @NonNull
    @Override
    public KodeVoucherAdapter.praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_kode_voucher, parent, false);

        return new KodeVoucherAdapter.praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull KodeVoucherAdapter.praktekholder holder, int position) {
        final Kode_voucher praktek = promo.get(position);
        holder.txt_1.setText(praktek.kode_voucher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}

