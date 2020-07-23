package com.example.mindhlju.game;

/**
 * Created by xiang
 * on 2018/11/14 19:08
 */
public class BlockCoordinateXY {
    private BlockView blockView;
    private int from;
    private int to;

    public BlockView getBlockView() {
        return blockView;
    }

    public void setBlockView(BlockView blockView) {
        this.blockView = blockView;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public BlockCoordinateXY(BlockView blockView, int from, int to) {
        this.blockView = blockView;
        this.from = from;
        this.to = to;
    }
}
