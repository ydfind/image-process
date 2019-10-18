package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;

/**
 * 傅里叶 遍历处理接口
 * @author ydfind
 * @date 2019.9.20
 */
@FunctionalInterface
public interface FourierHandler {

    /**
     * 颜色值遍历
     * @param complexes 傅里叶数组
     */
    int[][] handler(MyComplex[][] complexes);
}
