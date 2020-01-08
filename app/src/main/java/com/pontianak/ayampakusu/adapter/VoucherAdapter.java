package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pontianak.ayampakusu.DetailVoucherActivity;
import com.pontianak.ayampakusu.Helper;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.R;
import com.pontianak.ayampakusu.WebActivity;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.praktekholder> {
    public List<VoucherPelanggan> Vouchers;
    public Context ctx;
    public LayoutInflater lay;
    Helper helper;
    MyConfig myConfig = new MyConfig();
    AdapterInterface aie;

    public class praktekholder extends RecyclerView.ViewHolder {
        ImageView img_content;


        public praktekholder(View itemView) {
            super(itemView);
            this.img_content = itemView.findViewById(R.id.img_content);



        }
    }

    @Override
    public int getItemCount() {
        return this.Vouchers.size();
    }

    public VoucherAdapter(List<VoucherPelanggan> Vouchers, Context ctx) {
        this.Vouchers = Vouchers;
        this.ctx = ctx;
        this.lay = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        this.helper = new Helper(ctx);
        try{
            this.aie=((AdapterInterface)ctx);
        }
        catch (ClassCastException cce){

        }
    }

    @NonNull
    @Override
    public VoucherAdapter.praktekholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = lay.inflate(R.layout.content_list_imageonly, parent, false);

        return new VoucherAdapter.praktekholder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.praktekholder holder, final int position) {
        final VoucherPelanggan praktek = Vouchers.get(position);


        Glide.with(ctx).load(myConfig.main_url + "images/voucher/" + praktek.image_voucher).into(holder.img_content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aie.onselected(position);



            }
        });



    }
        public interface AdapterInterface{
         void onselected(int position);
    }
}
