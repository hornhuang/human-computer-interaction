package com.example.wxandroidapp.picture;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wxandroidapp.R;
import com.example.wxandroidapp.utils.Picture;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PictureShareAdapter extends RecyclerView.Adapter<PictureShareAdapter.PictureViewHoulder>{

    List<Picture> pics = new ArrayList<>();
    Context context;

    public PictureShareAdapter(Context context, List<Picture> pics) {
        this.context = context;
        this.pics = pics;
    }

    @NonNull
    @Override
    public PictureViewHoulder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp1_item_picture, parent, false);
        context = parent.getContext();
        return new PictureViewHoulder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHoulder holder, final int position) {
        Glide.with(context)
                .load(pics.get(position).getPicUrl())
                .placeholder(R.drawable.loading)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureDetailActivity.actionStart((SharePictureActivity)context, pics.get(position).getPicUrl());
            }
        });
        Log.d(TAG, pics.size() + "onBindViewHolder: " + pics.get(position).getPicUrl());
    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    class PictureViewHoulder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public PictureViewHoulder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
