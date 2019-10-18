package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 傅里叶处理
 * @author ydfind
 * @date 2019.10.18
 */
public class FourierProcessor extends ImageProcessor {

    /**
     * 将像素标定到0-255
     * @param pixels
     */
    public static void rating(int[][] pixels){
        int w = pixels.length;
        int h = pixels[0].length;
        // 找到最小最大值
        int min = pixels[0][0];
        int max = min;
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                if(pixels[i][j] < min){
                    min = pixels[i][j];
                }
                if(pixels[i][j] > max){
                    max = pixels[i][j];
                }
            }
        }
        // 标定
        double times = 255.0 / (max - min);
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                pixels[i][j] = new Double((pixels[i][j] - min) * times).intValue();
                if(pixels[i][j] < 0){
                    pixels[i][j] = 0;
                }
                if(pixels[i][j] > 255){
                    pixels[i][j] = 255;
                }
            }
        }
    }

    public static void process(BufferedImage srcImg, BufferedImage trgImg, BufferedImage newSrcImg,
                               FourierHandler FourierHandler){
        // 傅里叶正变换
        MyComplex[][] dest = DftProcessor.processDftForward(srcImg);
        // 根据截止频率过滤dest，并保存傅里叶幅度图片，
        int[][] newPixels = FourierHandler.handler(dest);
        DftProcessor.saveToImage(srcImg, trgImg, newPixels);
        // 傅里叶反变换，并保存图片
        DftProcessor.processAndSaveDftReverse(srcImg, newSrcImg, dest);
    }

    /**
     * 标志低通滤波器
     * @param srcFilename 原图像
     * @param trgFilename 傅里叶图像
     * @param newSrcFilename 目标图像
     * @param FourierHandler 傅里叶数组处理函数
     * @throws IOException 异常
     */
    public static void process(String srcFilename, String trgFilename, String newSrcFilename,
                               FourierHandler FourierHandler) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage fourierImg = copyImage(srcImg);
        BufferedImage trgImg = copyImage(srcImg);
        process(srcImg, fourierImg, trgImg, FourierHandler);
        ImgUtils.saveImage(trgFilename, fourierImg);
        ImgUtils.saveImage(newSrcFilename, trgImg);
    }
}
