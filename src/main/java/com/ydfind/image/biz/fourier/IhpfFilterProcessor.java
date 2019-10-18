package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 傅里叶 理想高通滤波器
 * @author ydfind
 * @date 2019.10.18
 */
public class IhpfFilterProcessor extends FourierProcessor {

    public static int[][] lowFilter(MyComplex[][] complexes, int radius, double a){
        int w = complexes.length;
        int h = complexes[0].length;
        int[][] pixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        int maxLen = radius * radius;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                int len =  new Double(Math.pow(x - cenX, 2.0) + Math.pow(y - cenY, 2.0)).intValue();
                double hParam = 0;
                int scope = 0;
                if(len > maxLen){
                    hParam = 1;
                }
                hParam = (a - 1) + hParam;
                complexes[i][j] = complexes[i][j].times(hParam);
                scope = new Double(complexes[i][j].abs()).intValue();
                // 显示更明显的图像
                scope = scope * 100;
                pixels[x][y] = scope;
            }
        }
        return pixels;
    }

//    public static void process(BufferedImage srcImg, BufferedImage scopeImg, BufferedImage highImg, BufferedImage trgImg,
//                               FourierHandler FourierHandler){
//        // 傅里叶正变换
//        MyComplex[][] dest = DftProcessor.processDftForward(srcImg);
//        // 频率，并保存傅里叶幅度图片，
//        int[][] newPixels = FourierHandler.handler(dest);
//        DftProcessor.saveToImage(srcImg, trgImg, newPixels);
//        // 傅里叶反变换，并保存图片
//        DftProcessor.processAndSaveDftReverse(srcImg, newSrcImg, dest);
//
//        // 锐化
//    }
//
//    public static void process(String srcFilename, String scopeFilename, String highFilename, String trgFilename,
//                               FourierHandler FourierHandler) throws IOException {
//        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
//        BufferedImage scopeImg = copyImage(srcImg);
//        BufferedImage highImg = copyImage(srcImg);
//        BufferedImage trgImg = copyImage(srcImg);
//
//        process(srcImg, scopeImg, highImg, trgImg, FourierHandler);
//
//        ImgUtils.saveImage(scopeFilename, scopeImg);
//        ImgUtils.saveImage(highFilename, highImg);
//        ImgUtils.saveImage(trgFilename, trgImg);
//    }

    public static void process(String srcFilename, String scopeFilename, String trgFilename,
                               int radius, double a) throws IOException {
        process(srcFilename, scopeFilename, trgFilename, (complexes) -> lowFilter(complexes, radius, a));
    }

    public static void process(String srcFilename, String scopeFilename, String trgFilename,
                               int radius) throws IOException {
        process(srcFilename, scopeFilename, trgFilename, radius, 1);
    }
}
