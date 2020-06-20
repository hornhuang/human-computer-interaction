package com.example.wxandroidapp.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.wxandroidapp.R;
import com.example.wxandroidapp.picture.SharePictureActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public static void actionStart(Activity activity) {
        activity.startActivity(new Intent(activity, GameActivity.class));
    }
}
