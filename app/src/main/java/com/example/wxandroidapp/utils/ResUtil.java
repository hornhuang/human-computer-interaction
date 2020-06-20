package com.example.wxandroidapp.utils;

import com.example.wxandroidapp.R;

public class ResUtil {

    public static int drawableRes[] = new int[] {
            R.mipmap.p11,
            R.mipmap.p21,
            R.mipmap.p31,
            R.mipmap.p41,
            R.mipmap.p12,
            R.mipmap.p22,
            R.mipmap.p32,
            R.mipmap.p42,
            R.mipmap.p13,
            R.mipmap.p23,
            R.mipmap.p33,
            R.mipmap.p43,
            R.mipmap.p14,
            R.mipmap.p24,
            R.mipmap.p34,
            R.mipmap.p44
    };

    public static int getMp3ByPosition(int position) {
        switch (position) {
            case 0: case 8:
                return R.raw.water;
            case 1:
                return R.raw.open_door;
            case 6: case 10:
                return R.raw.animal;
            case 12: case 14:
                return R.raw.bird;
            case 7: case 15:
                return R.raw.boat1;
            case 11:
                return R.raw.boat2;
            case 2: case 4: case 9:
                return R.raw.business;
            case 13:
                return R.raw.course_lesson;
            case 3: case 5:
                return R.raw.shop_boat_noise;
                default:
                    return R.raw.business;
        }
    }

    public static String getMp3TypeByPosition(int position) {
        switch (position) {
            case 0: case 8:
                return "小桥流水声";
            case 1:
                return "门进门出声";
            case 6: case 10:
                return "家鸭牲畜叫声";
            case 12: case 14:
                return "小鸟蝉鸣声";
            case 7: case 15:
                return "小船翩翩声";
            case 11:
                return "农夫划船声";
            case 2: case 4: case 9:
                return "市场嘈杂声";
            case 13:
                return "教室上课声";
            case 3: case 5:
                return "大船靠岸声";
            default:
                return "人来人往声";
        }
    }

}
