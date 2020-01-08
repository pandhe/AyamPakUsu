package com.pontianak.ayampakusu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    public class MenuHolder extends RecyclerView.ViewHolder{
        public MenuHolder(View viewholder){
            super(viewholder);

        }
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
