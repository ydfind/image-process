package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 傅里叶 高斯低通滤波器
 * @author ydfind
 * @date 2019.10.18
 */
public class GlpfFilterProcessor extends ImageProcessor {

    private static int[][] lowFilter(MyComplex[][] complexes, int radius){
        int w = complexes.length;
        int h = complexes[0].length;
        int[][] pixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        int lenRadius = radius * radius;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                double len = (x - cenX) * (x - cenX) + (y - cenY) * (y - cenY);
                double hLow = Math.exp(len / (-2 * lenRadius));
                complexes[i][j] = complexes[i][j].times(hLow);
                int scope = new Double(complexes[i][j].abs()).intValue();
                // 显示更明显的图像
                scope = scope * 100;
                pixels[x][y] = scope;
            }
        }
        return pixels;
    }

    /**
     * 高斯低通滤波器
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg, BufferedImage newSrcImg, int radius){
        // 傅里叶正变换
        MyComplex[][] dest = DftProcessor.processDftForward(srcImg);
        // 根据截止频率过滤dest，并保存傅里叶幅度图片，
        int[][] newPixels = lowFilter(dest, radius);
        DftProcessor.saveToImage(srcImg, trgImg, newPixels);
        // 傅里叶反变换，并保存图片
        DftProcessor.processAndSaveDftReverse(srcImg, newSrcImg, dest);
    }

    /**
     * 高斯低通滤波器
     * @param srcFilename 原图像
     * @param trgFilename 傅里叶图像
     * @param newSrcFilename 目标图像
     * @param radius 半径
     * @throws IOException 异常
     */
    public static void process(String srcFilename, String trgFilename, String newSrcFilename,
                               int radius) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage fourierImg = ImgUtils.copyImage(srcImg);
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        process(srcImg, fourierImg, trgImg, radius);
        ImgUtils.saveImage(trgFilename, fourierImg);
        ImgUtils.saveImage(newSrcFilename, trgImg);
    }
}
