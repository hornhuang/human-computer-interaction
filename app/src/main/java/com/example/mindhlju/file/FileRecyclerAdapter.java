package com.example.mindhlju.file;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileRecyclerAdapter extends RecyclerView.Adapter<FileRecyclerAdapter.FileViewHoulder>{


    @NonNull
    @Override
    public FileRecyclerAdapter.FileViewHoulder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecyclerAdapter.FileViewHoulder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FileViewHoulder extends RecyclerView.ViewHolder {

        public FileViewHoulder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
