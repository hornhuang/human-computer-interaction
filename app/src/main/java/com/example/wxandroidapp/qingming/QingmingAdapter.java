package com.example.wxandroidapp.qingming;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wxandroidapp.R;
import com.example.wxandroidapp.utils.Picture;
import com.example.wxandroidapp.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QingmingAdapter  extends RecyclerView.Adapter<QingmingAdapter.QingmingHoulder>{

    List<Picture> pics = new ArrayList<>();
    Context context;
    private MediaPlayer mediaPlayer;

    public QingmingAdapter(Context context, List<Picture> pics) {
        this.context = context;
        this.pics = pics;
    }

    @NonNull
    @Override
    public QingmingAdapter.QingmingHoulder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp2_item_picture, parent, false);
        context = parent.getContext();
        return new QingmingAdapter.QingmingHoulder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QingmingAdapter.QingmingHoulder holder, final int position) {
        if (position < 16) {
            holder.imageView.setImageResource(ResUtil.drawableRes[position]);
        }else {
//        Glide.with(context)
//                .load(pics.get(position).getPicUrl())
//                .placeholder(R.drawable.loading)
//                .into(holder.imageView);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ResUtil.getMp3TypeByPosition(position) , Toast.LENGTH_SHORT).show();
                Log.d("hronhuang", position + "");
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                openRawMusic(ResUtil.getMp3ByPosition(position));
            }
        });
//        holder.imageView.setImageResource(R.drawable.loading);
        Log.d(TAG, pics.size() + "onBindViewHolder: " + pics.get(position).getPicUrl());
    }

    /**
     * 打开raw目录下的音乐mp3文件
     */
    private void openRawMusic(int raw_id) {
        mediaPlayer = MediaPlayer.create(context, raw_id);
        //用prepare方法，会报错误java.lang.IllegalStateExceptio
        //mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    class QingmingHoulder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public QingmingHoulder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
