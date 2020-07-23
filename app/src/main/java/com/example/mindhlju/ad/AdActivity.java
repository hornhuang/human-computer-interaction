package com.example.mindhlju.ad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
