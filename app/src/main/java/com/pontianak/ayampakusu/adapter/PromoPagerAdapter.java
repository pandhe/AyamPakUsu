package com.pontianak.ayampakusu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pontianak.ayampakusu.MyConfig;
import com.pontianak.ayampakusu.R;

import java.util.ArrayList;

public class PromoPagerAdapter extends PagerAdapter {
    Context ctx;
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private MyConfig myConfig=new MyConfig();

    public PromoPagerAdapter(Context context, ArrayList<String> images) {
        this.ctx = context;
        this.images=images;
        inflater = (LayoutInflater)context.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v.equals(obj);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.content_list_promo, container, false);
        assert imageLayout != null;
        final ImageView img_slide=imageLayout.findViewById(R.id.img_promo);
        Glide.with(ctx).load(myConfig.main_url+"images/promo/"+images.get(position)).into(img_slide);


        container.addView(imageLayout);

         return imageLayout;
    }
    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((View) obj);
    }

}
