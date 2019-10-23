package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * DFT傅里叶正变换：目前默认传入的是黑白图片
 * @author ydfind
 * @date 2019.10.17
 */
@Component
@Service
public class DftProcessor extends ImageProcessor {

    /**
     * 离散傅里叶 正变换
     * @param x 图像x
     * @param y 图像y
     * @param u 变换后x
     * @param v 变换后y
     * @param w 图像长度
     * @param h 图像宽度
     * @return 计算结果
     */
    private static MyComplex getDftForwardItem(int x, int y, int u, int v, int w, int h){
        MyComplex complex = new MyComplex(0, Math.PI * -2 * (u * x * 1.0 / w + v * y * 1.0 / h));
        return complex.exp();
    }

    /**
     * 离散傅里叶 反变换
     * @param x 图像x
     * @param y 图像y
     * @param u 变换后x
     * @param v 变换后y
     * @param w 图像长度
     * @param h 图像宽度
     * @return 计算结果
     */
    private static MyComplex getDftReverseItem(int x, int y, int u, int v, int w, int h){
        MyComplex complex = new MyComplex(0, Math.PI * 2 * (u * x * 1.0 / w + v * y * 1.0 / h));
        return complex.exp();
    }

    /**
     * 计算傅里叶 正变换 中u，v出复数坐标
     * @param src 原图
     * @param u 傅里叶正变换后坐标x
     * @param v 傅里叶正变换后坐标y
     * @param w 图像宽度
     * @param h 图像高度
     * @return 计算结果
     */
    private static MyComplex calcUv(MyComplex src[][], int u, int v, int w, int h){
        MyComplex total = new MyComplex();
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                // 原图像灰度值保存在实部中，虚部默认为0
                total = total.plus(getDftForwardItem(i, j, u, v, w, h).times(src[i][j].getReal()));
            }
        }
        return total;
    }

    /**
     * 计算傅里叶 反变换 中u，v出复数坐标
     * @param src 原图
     * @param u 傅里叶正变换后坐标x
     * @param v 傅里叶正变换后坐标y
     * @param w 图像宽度
     * @param h 图像高度
     * @return 计算结果
     */
    private static MyComplex calcXy(MyComplex src[][], int u, int v, int w, int h){
        MyComplex total = new MyComplex();
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                // 原图像灰度值保存在实部中，虚部默认为0
                total = total.plus(getDftReverseItem(i, j, u, v, w, h).times(src[i][j]));
            }
        }
        return total;
    }

    /**
     * 根据原二维图像，进行傅里叶正变换
     * @param src 原图
     * @param desc 傅里叶变换后 结果
     * @param w 宽度
     * @param h 高度
     */
    private static void processDftForward(MyComplex src[][], MyComplex desc[][], int w, int h){
        double param = 1.0 / (w * h * 1.0);
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                desc[i][j] = calcUv(src, i, j, w, h).times(param);
            }
        }
    }

    /**
     * 根据原二维图像，进行傅里叶反变换
     * @param src 原图
     * @param desc 傅里叶变换后 结果
     * @param w 宽度
     * @param h 高度
     */
    private static void processDftReverse(MyComplex src[][], MyComplex desc[][], int w, int h){
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                desc[i][j] = calcXy(src, i, j, w, h);
            }
        }
    }

    /**
     * 傅里叶 反变换 并保存
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param dest 频率谱数组
     */
    public static void processAndSaveDftReverse(BufferedImage srcImg, BufferedImage trgImg, MyComplex[][] dest){
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        // 进行反变换
        MyComplex[][] newSrc = new MyComplex[w][h];
        processDftReverse(dest, newSrc, w, h);
        int[][] pixels = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int scope = new Double(newSrc[i][j].abs()).intValue();
                pixels[i][j] = scope;
            }
        }
        saveToImage(srcImg, trgImg, pixels);
    }

    /**
     * 离散傅里叶 正变换
     * @param srcImg 原图像
     * @return 处理后傅里叶 频率坐标
     */
    public static MyComplex[][] processDftForward(BufferedImage srcImg){
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        // 根据原图生成二维数组
        MyComplex[][] src = new MyComplex[w][h];
        int[][] pixels = getImageGray(srcImg);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                src[i][j] = new MyComplex(pixels[i][j], 0);
            }
        }

        // 进行傅里叶正变换
        MyComplex[][] dest = new MyComplex[w][h];
        processDftForward(src, dest, w, h);
        return dest;
    }

    /**
     * 中值滤波器
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg, BufferedImage newSrcImg, int threshold){
        // 傅里叶正变换
        MyComplex[][] dest = processDftForward(srcImg);
        // 根据截止频率过滤dest，并保存傅里叶幅度图片
        int[][] newPixels = extractScope(dest, threshold);
        saveToImage(srcImg, trgImg, newPixels);
        // 傅里叶反变换，并保存图片
        processAndSaveDftReverse(srcImg, newSrcImg, dest);
    }

    /**
     * 提取频谱
     * @param complexes 离散傅里叶正变换后坐标
     * @return 幅度
     */
    private static int[][] extractScope(MyComplex[][] complexes, int threshold){
        int w = complexes.length;
        int h = complexes[0].length;
        int[][] pixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int scope = new Double(complexes[i][j].abs()).intValue();
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                // 过滤
                if(scope > threshold){
//                    pixels[x][y] = threshold;
//                    complexes[i][j].times(threshold / scope);
                    pixels[x][y] = 0;
                    complexes[i][j] = new MyComplex(0, 0);
                }else{
                    // 显示更明显的图像
                    scope = scope * 100;
                    pixels[x][y] = scope;
                }
//                if(scope < threshold){
//                    pixels[x][y] = 0;
//                    complexes[i][j] = new MyComplex(0, 0);
//                }
            }
        }
        return pixels;
    }

    /**
     * 将图像灰度值保存到 目标图像
     * @param srcImg 原图像
     * @param trgImg 目标图像
     * @param pixels 像素值
     */
    public static void saveToImage(BufferedImage srcImg, BufferedImage trgImg, int[][] pixels){
        int w = pixels.length;
        int h = pixels[0].length;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (pixels[i][j] > 255) {
                    pixels[i][j] = 255;
                }
            }
        }
        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
            for(int k = 1; k < ImgUtils.COLOR_CHANNEL_COUNT; k++){
                channels[k] = pixels[x][y];
            }
            trgImg.setRGB(x, y, ImgUtils.colorToRgb(channels));
        });
    }

    /**
     * 中值滤波器
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void process(String srcFilename, String trgFilename, String newSrcFilename,
                               int threshold) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        BufferedImage newSrcImg = ImgUtils.copyImage(srcImg);
        process(srcImg, trgImg, newSrcImg, threshold);
        ImgUtils.saveImage(trgFilename, trgImg);
        ImgUtils.saveImage(newSrcFilename, newSrcImg);
    }

}
