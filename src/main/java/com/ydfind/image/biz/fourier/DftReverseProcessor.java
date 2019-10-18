package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * DFT傅里叶反变换：目前默认传入的是黑白图片
 * @author ydfind
 * @date 2019.10.17
 */
@Component
@Service
public class DftReverseProcessor extends ImageProcessor {

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
    private static MyComplex getDftItem(int x, int y, int u, int v, int w, int h){
        MyComplex complex = new MyComplex(0, Math.PI * 2 * (u * x * 1.0 / w + v * y * 1.0 / h));
        return complex.exp();
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
                total = total.plus(getDftItem(i, j, u, v, w, h).times(src[i][j].getReal()));
            }
        }
        return total;
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
     * 中值滤波器
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg){
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        // 根据原图生成二维数组
        MyComplex[][] src = new MyComplex[w][h];
        MyComplex[][] dest = new MyComplex[w][h];
        int[][] pixels = getImageGray(srcImg);
        // 初始化src,dest
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                dest[i][j] = new MyComplex();
                src[i][j] = new MyComplex(pixels[i][j], 0);
            }
        }
        // 进行傅里叶反变换
        processDftReverse(src, dest, w, h);
        // 提取频谱图像-幅度谱
        int[][] newPixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int x;
                int y;
                int scope = new Double(dest[i][j].getReal()).intValue();
                // 显示更明显的图像
                if (scope > 255) {
                    scope = 255;
                }
                x = i < cenX ? i + cenX : i - cenX;
                y = j < cenY ? j + cenY : j - cenY;
                newPixels[x][y] = scope;
            }
        }
        // 保存到新图片
        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
            for(int k = 1; k < ImgUtils.COLOR_CHANNEL_COUNT; k++){
                channels[k] = newPixels[x][y];
            }
            int newColor = ImgUtils.colorToRgb(channels);
            trgImg.setRGB(x, y, newColor);
        });
    }

    /**
     * 中值滤波器
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void process(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = srcImg.getSubimage(0, 0, srcImg.getWidth(), srcImg.getHeight());
        process(srcImg, trgImg);
        // 结果进行线性处理
        int[] colorExtremeValues = ImgUtils.getColorExtremeValues(trgImg);
        int srcStart = colorExtremeValues[0];
        int srcEnd = colorExtremeValues[1];
        final int trgStart = 0;
        final int trgRange = 255 - trgStart;
        grayLineTransformation(trgImg, trgImg, (gray) -> {
            if(gray < srcStart){
                return trgStart;
            }else if(gray > srcEnd){
                return trgStart + trgRange;
            }else{
                int newGray = new Double(
                        ((trgRange * 1.0) / (srcEnd - srcStart)) * (gray - srcStart) + trgStart).intValue();
                return newGray;
            }
        });
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
