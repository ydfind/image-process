package com.ydfind.image.biz;

/**
 * 图像遍历接口
 * @author ydfind
 * @date 2019.9.20
 */
@FunctionalInterface
public interface ImageColorTraverse {

    /**
     * 颜色值遍历
     * @param x 当前像素点x
     * @param y 当前像素点y
     * @param color 颜色值
     * @param channels 各个通道的值
     * @return 返回调整后灰度值
     */
    void traverse(int x, int y, int color, int[] channels);
}
