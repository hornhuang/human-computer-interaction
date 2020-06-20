package com.example.wxandroidapp.qingming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.wxandroidapp.R;
import com.example.wxandroidapp.picture.PictureShareAdapter;
import com.example.wxandroidapp.picture.SharePictureActivity;
import com.example.wxandroidapp.utils.DataUtil;

public class QingmingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingming);

        recyclerView = findViewById(R.id.pic_recycler);
        QingmingAdapter adapter = new QingmingAdapter(this, DataUtil.getPictures());
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public static void actionStart(Activity activity) {
        activity.startActivity(new Intent(activity, QingmingActivity.class));
    }
}
