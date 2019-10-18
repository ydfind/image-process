package com.ydfind.image.biz.fourier;

/**
 * 傅里叶 遍历处理接口
 * @author ydfind
 * @date 2019.9.20
 */
@FunctionalInterface
public interface ImageDftHandler {

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
