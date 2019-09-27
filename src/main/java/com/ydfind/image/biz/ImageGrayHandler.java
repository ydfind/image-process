package com.ydfind.image.biz;

/**
 * 图像灰度处理
 * @author ydfind
 * @date 2019.9.20
 */
@FunctionalInterface
public interface ImageGrayHandler {

    /**
     * 灰度值调整
     * @param gray 原灰度值
     * @return 返回调整后灰度值
     */
    int getNewGray(int gray);

}
