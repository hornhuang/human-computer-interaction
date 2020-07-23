package com.example.mindhlju.game;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by xiang
 * on 2018/11/12 17:04
 */
public class GameView2048 extends GridLayout {
    //初始 默认
    private int rowsCount = 4;
    private int clonmsCount = 4;
    /*
     * 数据集合
     * */
    private List<int[]> datas = new ArrayList<>();
    /*
     * 缓存数据集合 设置只能撤销一步
     * */
    private List<int[]> cachedatas = new ArrayList<>();
    private float down_rowx;
    private float down_rowy;
    private String direction;
    private float distance_x;
    private float distance_y;
    private Random random = new Random();
    private LayoutParams params;
    private int[] clone;
    private int[] clonecache;
    private int space = 2;
    private float density;
    private int blockSideLength;
    private List<Animator> objectAnimatorList;
    private List<PointF> pointFS = new ArrayList<>();
    private float radius = 10f;
    private boolean isLayout = false;
    private float distance = 0;
    private List<BlockCoordinateXY> startblockCoordinateXYList = new ArrayList<>();
    /*是否是新游戏*/
    private boolean isNew = true;
    private int randomIndex = -1;
    //产生加法的格子
    private List<Integer> blockindex = new ArrayList<>();
    private int knowscore = 0;//时时分数
    private int score = knowscore;//分数
    private int addscore = 0;//增加的分数 撤销时减掉
    private float multiple = 1;
    private boolean isOver = false;

    public GameView2048(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GameView2048(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView2048(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        if (rowsCount < 1 || clonmsCount < 1) {
            return;
        }
        float width = 800;
        float height = 800;
        if (View.MeasureSpec.getMode(widthSpec) == View.MeasureSpec.EXACTLY) {
            width = View.MeasureSpec.getSize(widthSpec);
        }
        if (View.MeasureSpec.getMode(heightSpec) == View.MeasureSpec.EXACTLY) {
            height = View.MeasureSpec.getSize(heightSpec);
        }
        int viewHeight, viewWidth;
//        if (rowsCount * 1f / clonmsCount> height / width) {
//            viewHeight = (int) height;
//            blockSideLength = (viewHeight - space * 2 - rowsCount * 2 * space) / rowsCount;
//            viewWidth = clonmsCount * blockSideLength + clonmsCount * 2 * space + 2 * space;
//        } else {
        viewWidth = (int) width;
        blockSideLength = (viewWidth - space * 2 - clonmsCount * 2 * space) / clonmsCount;
        viewHeight = rowsCount * blockSideLength + rowsCount * 2 * space + 2 * space;
//        }
//        for (int i = 0; i < getChildCount(); i++) {
//            GridLayout.LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
//            layoutParams.setMargins(space, space, space, space);
//            getChildAt(i).setLayoutParams(layoutParams);
//            getChildAt(i).measure(View.MeasureSpec.makeMeasureSpec(blockSideLength, View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(blockSideLength, View.MeasureSpec.EXACTLY));
//        }

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isLayout) {
            for (int i = 0, ii = getChildCount(); i < ii; i++) {
                pointFS.add(new PointF(getChildAt(i).getX(), getChildAt(i).getY()));
            }
            isLayout = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#BBADA0"));
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setColor(Color.parseColor("#CDC1B4"));
        for (PointF pointF : pointFS) {
            rectF = new RectF(pointF.x, pointF.y, pointF.x + blockSideLength, pointF.y + blockSideLength);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }

    @SuppressLint("NewApi")
    private void init() {

        rowsCount = getRowCount() == 0 ? rowsCount : getRowCount();//多少行
        clonmsCount = getColumnCount() == 0 ? clonmsCount : getColumnCount();//多少列

        if (SharePreferenceUtil.getGameDatas(getContext()) != null) {
            datas = SharePreferenceUtil.getGameDatas(getContext());
            knowscore = SharePreferenceUtil.getIntValue(getContext(), "score");
        } else {
            initData();
        }

        density = getContext().getResources().getDisplayMetrics().density;
        space = (int) (density * 2 + 0.5);
        setTextData();
        setPadding(space, space, space, space);
    }

    private void initData() {
        //行决定 datas有多少条数据 列决定 每条数据（数组）有多少元素
        for (int i = 0; i < rowsCount; i++) {
            datas.add(new int[clonmsCount]);//初始化数据 这时全是0
        }

        //随机两个位置设置为2
        int randomrow1 = random.nextInt(rowsCount);
        int randomclonm1 = random.nextInt(clonmsCount);
        datas.get(randomrow1)[randomclonm1] = 2;

        int randomrow2 = random.nextInt(rowsCount);
        int randomclonm2 = random.nextInt(clonmsCount);
        //while循环防止两个随机行列坐标相同
        while (randomrow2 == randomrow1 && randomclonm2 == randomclonm1) {
            randomrow2 = random.nextInt(rowsCount);
            randomclonm2 = random.nextInt(clonmsCount);
        }
        datas.get(randomrow2)[randomclonm2] = 2;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                down_rowx = event.getRawX();
                down_rowy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                distance_x = Math.abs(down_rowx - event.getRawX());
                distance_y = Math.abs(down_rowy - event.getRawY());
                if (distance_y >= 30 || distance_x >= 30) {
                    if (distance_y >= distance_x) {
                        if (down_rowy > event.getRawY()) {//向上

                            direction = "up";
                        } else {//向下
                            direction = "down";
                        }
                    } else {
                        if (down_rowx > event.getRawX()) {//向左
                            direction = "left";
                        } else {//向右
                            direction = "right";
                        }

                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (direction != null && direction.length() > 0 && !isOver) {
                    reset();

                    switch (direction) {
                        case "up":
                            up();
                            break;
                        case "down":
                            down();
                            break;
                        case "right":
                            right();
                            break;
                        case "left":
                            left();
                            break;
                    }
                }
                direction = "";
                break;
        }
        return true;
    }

    /*重置状态*/
    private void reset() {
//        if(!cacheExist()){
        //缓存数据
        cachedatas.clear();
        for (int i = 0; i < datas.size(); i++) {
            cachedatas.add(datas.get(i).clone());
        }
//        }
        isNew = false;
        startblockCoordinateXYList.clear();
        blockindex.clear();
    }

    /*缓存的上一步是否和这一步一样*/
    private boolean cacheExist() {
        if (cachedatas.size() <= 0) {
            return false;
        }
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < clonmsCount; j++) {
                if (datas.get(i)[j] != cachedatas.get(i)[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * 向上滑动
     * */
    private void up() {
        clone = new int[rowsCount];
        //上下都是 遍历列
        for (int i = 0; i < clonmsCount; i++) {

            for (int j = 0; j < rowsCount; j++) {
                clone[j] = datas.get(j)[i];
            }
            clonecache = new int[clone.length];
            int left = 0;//左侧开始计算
            int right = clone.length - 1;//右侧开始计算
            int data = 0;
            // 数据上移
            for (int j = 0, jj = clone.length; j < jj; j++) {
                if (clone[j] == 0) {
                    clonecache[right--] = 0;
                } else {
                    BlockCoordinateXY blockCoordinateXY = null;
                    if (data == clone[j]) {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(j * clonmsCount + i), (j * clonmsCount + i), ((left - 1) * clonmsCount + i));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[left - 1] = data + clone[j];
                        addMultiple(data + clone[j]);
                        data = 0;
                        blockindex.add(((left - 1) * clonmsCount + i));
                    } else {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(j * clonmsCount + i), (j * clonmsCount + i), (left * clonmsCount + i));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[left++] = clone[j];
                        data = clone[j];
                    }
                }
            }
            for (int j = 0; j < clonecache.length; j++) {
                datas.get(j)[i] = clonecache[j];
            }

        }
        preAnimation("translationY");
    }

    /*
     * 向下滑动
     * */
    private void down() {
        clone = new int[rowsCount];
        //上下都是 遍历列
        for (int i = 0; i < clonmsCount; i++) {

            for (int j = 0; j < rowsCount; j++) {
                clone[j] = datas.get(j)[i];
            }
            clonecache = new int[clone.length];
            int left = 0;//左侧开始计算
            int right = clone.length - 1;//右侧开始计算
            int data = 0;
            // 数据下移
            for (int j = clone.length - 1; j >= 0; j--) {
                if (clone[j] == 0) {
                    clonecache[left++] = 0;
                } else {
                    BlockCoordinateXY blockCoordinateXY = null;
                    if (data == clone[j]) {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(j * clonmsCount + i), (j * clonmsCount + i), ((right + 1) * clonmsCount + i));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[right + 1] = data + clone[j];
                        addMultiple(data + clone[j]);
                        data = 0;
                        blockindex.add(((right + 1) * clonmsCount + i));
                    } else {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(j * clonmsCount + i), (j * clonmsCount + i), (right * clonmsCount + i));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[right--] = clone[j];
                        data = clone[j];
                    }
                }
            }
            for (int j = 0; j < clonecache.length; j++) {
                datas.get(j)[i] = clonecache[j];
            }
        }
        preAnimation("translationY");
    }

    /*
      向左滑动
    * */
    private void left() {
        //左右都是 遍历行
        for (int i = 0; i < rowsCount; i++) {
            clone = datas.get(i).clone();
            //数据左移
            clonecache = new int[clone.length];
            datas.remove(i);
            int left = 0;//左侧开始计算
            int right = clone.length - 1;//右侧开始计算
            int data = 0;
            //将[0,2,0,0] or [0,2,2,0] 这种 先转换为 [2,0,0,0] or [4,0,0,0] 即 数据左移
            for (int j = 0, jj = clone.length; j < jj; j++) {
                if (clone[j] == 0) {
                    clonecache[right--] = 0;
                } else {
                    BlockCoordinateXY blockCoordinateXY = null;
                    if (data == clone[j]) {//如果这个值 与 前面保存的 不为0的值相同 就会相加 (碰撞相加)
                        //添加会运动的格子
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(i * clone.length + j), (i * clone.length + j), (i * clone.length + left - 1));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[left - 1] = data + clone[j];
                        //计算分数
                        addMultiple(data + clone[j]);
                        data = 0;
                        //添加产生加法的格子
                        blockindex.add(i * clone.length + left - 1);
                    } else {
                        //添加会运动的格子
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(i * clone.length + j), (i * clone.length + j), (i * clone.length + left));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        //保存这一个不为0的值
                        data = clone[j];
                        clonecache[left++] = clone[j];
                    }
                }
            }
            datas.add(i, clonecache);
        }
        preAnimation("translationX");
    }

    /*
       向右滑动
     * */
    private void right() {
        //左右都是 遍历行
        for (int i = 0; i < rowsCount; i++) {
            clone = datas.get(i).clone();
            //数据左移
            clonecache = new int[clone.length];
            datas.remove(i);
            int left = 0;//左侧开始计算
            int right = clone.length - 1;//右侧开始计算
            int data = 0;
            for (int j = clone.length - 1; j >= 0; j--) {
                if (clone[j] == 0) {
                    clonecache[left++] = 0;
                } else {
                    BlockCoordinateXY blockCoordinateXY = null;
                    if (data == clone[j]) {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(i * clone.length + j), (i * clone.length + j), (i * clone.length + right + 1));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        clonecache[right + 1] = data + clone[j];
                        addMultiple(data + clone[j]);
                        data = 0;
                        blockindex.add(i * clone.length + right + 1);
                    } else {
                        blockCoordinateXY = new BlockCoordinateXY((BlockView) getChildAt(i * clone.length + j), (i * clone.length + j), (i * clone.length + right));
                        startblockCoordinateXYList.add(blockCoordinateXY);
                        data = clone[j];
                        clonecache[right--] = clone[j];
                    }

                }
            }
            datas.add(i, clonecache);
        }
        preAnimation("translationX");
    }


    /*分数增加*/
    private void addMultiple(int num) {
        //指数是2的多少倍 分数增加 10 * multiple
        multiple = (float) (Math.log(num) / Math.log(2));
        knowscore += (int) (10 * multiple);
    }


    private void getRandomData() {
        List<Map<Integer, Integer>> empty_datas = new ArrayList<>();
        for (int i = 0, ii = datas.size(); i < ii; i++) {
            final int[] intarr = datas.get(i);
            for (int j = 0, jj = intarr.length; j < jj; j++) {
                if (intarr[j] == 0) {
                    Map<Integer, Integer> map = new HashMap<>();
                    map.put(i, j);
                    //先将为0的元素添加到新的集合
                    empty_datas.add(map);
                }
            }
        }
        if (empty_datas.size() <= 0) {//没有空余的格子了 判断游戏是否结束
            return;
        }
        //随机一个为 0 的元素 设置为 2
        Map<Integer, Integer> map = empty_datas.get(random.nextInt(empty_datas.size()));
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            //产生 2 的概率是百分之80
            datas.get(entry.getKey())[entry.getValue()] = random.nextInt(5) > 3 ? 4 : 2;
            //记录随机的格子索引
            randomIndex = entry.getKey() * clonmsCount + entry.getValue();
        }
        if (empty_datas.size() == 1) {
            if (gameOver()) {//游戏是否结束
                Toast.makeText(getContext(), "gameover，太弱了", Toast.LENGTH_LONG).show();
                isOver = true;
            }
        }
        //更新textview文字
        setTextData();
        empty_datas.clear();
    }

    /*
     * 更新textview文字*/
    private void setTextData() {
        //先移除子view
        removeAllViews();
        for (int i = 0, ii = datas.size(); i < ii; i++) {
            final int[] intarr = datas.get(i);
            for (int j = 0, jj = intarr.length; j < jj; j++) {
                BlockView textView = new BlockView(getContext());
                params = new GridLayout.LayoutParams();
                textView.setText(intarr[j]);
                // 设置行列下标，和比重
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                params.setMargins(space, space, space, space);
                //这两句要加 不加宽度会出问题
                params.width = 0;
                params.height = 0;
                textView.setLayoutParams(params);
                addView(textView, params);

            }
        }
        if (!isNew && blockindex.size() > 0) {
            if (objectAnimatorList == null) {
                objectAnimatorList = new ArrayList<>();
            } else {
                objectAnimatorList.clear();
            }
            for (int i = 0; i < blockindex.size(); i++) {
                @SuppressLint("ObjectAnimatorBinding") ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getChildAt(blockindex.get(i)), "scale", 1f, 1.2f, 1f);
                objectAnimatorList.add(objectAnimator);
            }
            if (objectAnimatorList.size() > 0) {
                starScaleAnimate();
            }
        } else {
            starRandomScaleAnimate();
        }


    }

    /*格子动画*/
    private void starAnimate() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimatorList);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //更新分数
                if (onGameNotifyListener != null) {
                    addscore = knowscore - score;//增加的分数
                    score = knowscore;//保存这一步的分数
                    onGameNotifyListener.onscore(score);
                    if (getHighScore() < score) {
                        SharePreferenceUtil.saveInt(getContext(), "highScore", score);
                        onGameNotifyListener.onhighestscore(score);
                    }
                }

                //产生随机数
                getRandomData();
            }
        });
        set.setDuration(150);
        set.start();
    }

    /*产生加法的格子动画*/
    private void starScaleAnimate() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimatorList);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                starRandomScaleAnimate();
            }
        });
        set.setDuration(100);
        set.start();
    }

    /*最忌产生的格子动画*/
    private void starRandomScaleAnimate() {
        //随机产生的那个有缩放动画
        if (!isNew && randomIndex >= 0) {
            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getChildAt(randomIndex), "scale", 1f, 1.2f, 1f);
            objectAnimator.setDuration(100);
            objectAnimator.start();
            randomIndex = -1;
        }
    }

    /*准备动画 添加动画集合*/
    private void preAnimation(String direction) {
        if (objectAnimatorList == null) {
            objectAnimatorList = new ArrayList<>();
        } else {
            objectAnimatorList.clear();
        }

        for (int i = 0; i < startblockCoordinateXYList.size(); i++) {
            ObjectAnimator objectAnimator = null;
            BlockCoordinateXY blockCoordinateXY = startblockCoordinateXYList.get(i);
            if (blockCoordinateXY != null) {
                if (direction.equals("translationX")) {
                    distance = pointFS.get(blockCoordinateXY.getTo()).x + blockSideLength
                            - (pointFS.get(blockCoordinateXY.getFrom()).x + blockSideLength);

                } else {
                    distance = pointFS.get(blockCoordinateXY.getTo()).y + blockSideLength
                            - (pointFS.get(blockCoordinateXY.getFrom()).y + blockSideLength);

                }
                objectAnimator = ObjectAnimator.ofFloat(blockCoordinateXY.getBlockView(), direction, distance);
                objectAnimatorList.add(objectAnimator);
            }
        }

        if (objectAnimatorList.size() > 0) {
            starAnimate();
        }
    }

    /*游戏结束判断*/
    private boolean gameOver() {

        //循环行 有相同元素即 还未结束
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0, jj = clonmsCount - 1; j < jj; j++) {
                if (datas.get(i)[j] == datas.get(i)[j + 1]) {
                    return false;
                }
            }
        }
        //循环列
        for (int i = 0, ii = clonmsCount; i < ii; i++) {
            for (int j = 0, jj = rowsCount - 1; j < jj; j++) {
                if (datas.get(j)[i] == datas.get(j + 1)[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getHighScore() {
        return SharePreferenceUtil.getIntValue(getContext(), "highScore");
    }

    /*退出时保存*/
    public void setCache() {
        SharePreferenceUtil.saveGameDatas(getContext(), datas);
        SharePreferenceUtil.saveInt(getContext(), "score", knowscore);
    }

    /*撤销*/
    public void revoke() {
        if (isNew) {//新游戏
            return;
        }
        if (cachedatas.size() <= 0) {
            Toast.makeText(getContext(), "只能撤销一步！", Toast.LENGTH_SHORT).show();
            return;
        }
        isOver = false;
        datas.clear();
        datas.addAll(cachedatas);
        setTextData();
        cachedatas.clear();
        if (onGameNotifyListener != null) {
            knowscore -= addscore;//减去这一步增加的分数
            score = knowscore;
            onGameNotifyListener.onscore((int) (knowscore));
        }
    }

    /*清除缓存*/
    public boolean clearCache() {
        SharePreferenceUtil.clear(getContext());
        if (onGameNotifyListener != null) {
            onGameNotifyListener.onhighestscore(0);
        }
        return true;
    }

    /*新游戏*/
    public boolean newGame() {
        reset();
        isNew = true;
        isOver = true;
        datas.clear();
        initData();
        setTextData();
        knowscore = addscore = score = 0;
        if (onGameNotifyListener != null) {
            onGameNotifyListener.onscore(knowscore);
        }
        return true;
    }

    //分数更新监听
    public void setOnGameNotifyListener(OnGameNotifyListener onGameNotifyListener) {
        this.onGameNotifyListener = onGameNotifyListener;
    }

    private OnGameNotifyListener onGameNotifyListener;

    public interface OnGameNotifyListener {
        void onscore(int score);//当前分数

        void onhighestscore(int highscore);//最高分
    }
}
