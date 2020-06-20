package com.example.wxandroidapp.music;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wxandroidapp.picture.PictureShareAdapter;

public class MusicRecyclerAdapter extends RecyclerView.Adapter<MusicRecyclerAdapter.MusicViewHoulder>{


    @NonNull
    @Override
    public MusicRecyclerAdapter.MusicViewHoulder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicRecyclerAdapter.MusicViewHoulder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MusicViewHoulder extends RecyclerView.ViewHolder {

        public MusicViewHoulder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
