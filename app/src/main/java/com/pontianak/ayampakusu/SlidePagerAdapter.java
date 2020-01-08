package com.pontianak.ayampakusu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SlidePagerAdapter extends PagerAdapter {
    Context ctx;
    private ArrayList<String> images;
    private ArrayList<String> detail;
    private LayoutInflater inflater;

    public SlidePagerAdapter(Context context,ArrayList<String> images,ArrayList<String> detail) {
        this.ctx = context;
        this.images=images;
        this.detail=detail;
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
        View imageLayout = inflater.inflate(R.layout.welcome_slide1, container, false);
        assert imageLayout != null;
        final ImageView img_slide=imageLayout.findViewById(R.id.img_slide);
        final TextView text_slide=imageLayout.findViewById(R.id.txt_deskripsi);
        String uri = "drawable/";

        int imageResource = ctx.getResources().getIdentifier(uri+images.get(position), null, ctx.getPackageName());

        Drawable image = ctx.getResources().getDrawable(imageResource);
        img_slide.setImageDrawable(image);
        text_slide.setText(detail.get(position));
        container.addView(imageLayout);

         return imageLayout;
    }
    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((View) obj);
    }

}
