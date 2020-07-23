package com.example.mindhlju.ad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mindhlju.MainActivity;
import com.example.mindhlju.R;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTimeText;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        initViews();
        setStatusBarTranslation();

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String tips = "跳过： " + ( millisUntilFinished / 1000 + 1 );
                mTimeText.setText(tips);
            }

            @Override
            public void onFinish() {
                jumpOut(null);
            }
        }.start();
    }

    private void initViews() {
        mTimeText = findViewById(R.id.time_count);

        mTimeText.setOnClickListener(this);
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

    public synchronized void jumpOut(View view) {
        if (countDownTimer == null) {
            return;
        }
        countDownTimer = null;
        startActivity(new Intent(AdActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_count:
                jumpOut(null);
        }
    }
}
