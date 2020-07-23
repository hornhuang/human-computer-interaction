package com.example.mindhlju.game;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiang
 * on 2018/11/14 14:11
 */
public class SharePreferenceUtil {
    /**
     * SharedPreference名称
     */
    private static final String PREFERENCE_FILE_NAME = "data2048_preferences";
    private static final String GAME_DATA="game_datalist";

    public static void saveInt(final Context context, String key, int value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getInt(key, 0);
    }

    public static void saveGameDatas(final Context context, final List<int[]> gamedatas) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putString(GAME_DATA, new Gson().toJson(gamedatas));
        editor.commit();
    }


    public static List<int[]> getGameDatas(final Context context) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String data = preference.getString(GAME_DATA, null);
        List<int[]> fromJson = null;
        if (data != null && data.length() > 0) {
            Type listType = new TypeToken<List<int[]>>() {
            }.getType();
            Gson gson = new Gson();
            fromJson = gson.fromJson(data, listType);
        }
        return fromJson;
    }

    public static void clear(final Context context) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.clear();
        editor.commit();
    }
}
