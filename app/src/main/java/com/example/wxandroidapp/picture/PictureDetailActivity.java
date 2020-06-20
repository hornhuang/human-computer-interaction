package com.example.wxandroidapp.picture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wxandroidapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PictureDetailActivity extends AppCompatActivity {

    private ImageView image;
    private static final String URL = "web_url";
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);

        getSupportActionBar().hide();
        setStatusBarTranslation();

        image = findViewById(R.id.webview);
        Glide.with(this).load(getIntent().getStringExtra(URL)).into(image);

        floatingActionButton = findViewById(R.id.fab);

        Glide.with(this)
                .asBitmap() //必须
                .load(getIntent().getStringExtra(URL))
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        createPaletteAsync(resource);
                    }
                });




    }

    protected void setStatusBarTranslation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置状态栏透明
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
            //设置导航栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置导航栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // Generate palette synchronously and return it
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    // Generate palette asynchronously and use it on a different
    // thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                floatingActionButton.setBackgroundColor(p.getMutedColor(Color.GRAY));
            }
        });
    }

    public void share(View view) {
        showNormalDialog();
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PictureDetailActivity.this);
//        normalDialog.setIcon(R.drawable.share);
        normalDialog.setTitle("确定分享这张图吗？");
        normalDialog.setMessage("朋友们都在等主人的美图呦～");
        normalDialog.setNegativeButton("再想想",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setPositiveButton("立刻分享～",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PictureDetailActivity.this, "Jarvis 已为您成功分享哦～", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    public static void actionStart(Activity activity, @NonNull String picUrl) {
        Intent intent = new Intent(activity, PictureDetailActivity.class);
        intent.putExtra(URL, picUrl);
        activity.startActivity(intent);
    }
}
