package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 包含图像的基本操作类
 * @author ydfind
 * @date 2019.9.20
 */
public class ImageProcessor {

    /**
     * 图像灰度 线性处理
     * @param srcImg 原图像
     * @param trgImg 处理后保存图像
     * @param handler 处理函数
     */
    public static void grayLineTransformation(BufferedImage srcImg, BufferedImage trgImg, ImageGrayHandler handler) {
        processGgbChannels(srcImg, trgImg, handler);
    }

    /**
     * 图像灰度 线性处理
     * @param srcFilename 原图像
     * @param trgFilename 处理后保存图像
     * @param handler 处理函数
     */
    public static void grayLineTransformation(String srcFilename, String trgFilename,
                                        ImageGrayHandler handler) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        grayLineTransformation(srcImg, trgImg, handler);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * 直方图均衡
     * @param srcFilename 原图像
     * @param trgFilename 处理后保存图像
     */
    public static void equalizeHist(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        equalizeHist(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * 直方图均衡
     * @param srcImg 原图像
     * @param trgImg 目标图像
     */
    public static void equalizeHist(BufferedImage srcImg, BufferedImage trgImg){
        // 找到图像最值
        int[][] extremeValues = ImgUtils.getChannelExtremeValues(srcImg);
        // 计算
        double[][] counts = ImgUtils.statColorChannels(srcImg);
        int total = srcImg.getHeight() * srcImg.getWidth();
        double[] curS = new double[3];
        for(int i = 0; i < ImgUtils.COLOR_CHANNEL_COUNT - 1; i++){
            for(int j = 0; j < ImgUtils.PIXEL_MAX_VALUE; j++){
                counts[i][j] = counts[i][j] / total;
                curS[i] += counts[i][j];
                counts[i][j] = curS[i] * extremeValues[i][1] - extremeValues[i][0];
                if(counts[i][j] < 0 || counts[i][j] >= 256){
                    System.out.println("结果不正确：" + counts[i][j]);
                }
            }
        }
        // 赋值新图片
        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
            int newColor = ImgUtils.colorToRgb(channels[0],
//                    counts[0][channels[1]], counts[1][channels[2]], counts[2][channels[3]]);
            counts[2][channels[1]], counts[1][channels[2]], counts[0][channels[3]]);
            trgImg.setRGB(x, y, newColor);
        });
    }

    /**
     * 对数变换
     * @param srcFilename 原图像
     * @param trgFilename 处理后保存图像
     * @param mut 255缩小的倍数
     */
    public static void logarithmTransformation(String srcFilename, String trgFilename, double mut)
            throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        logarithmTransformation(srcImg, trgImg, mut);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * 对数变换
     * @param srcImg 原图像
     * @param trgImg 目标图像
     * @param mut 255缩小的倍数
     */
    public static void logarithmTransformation(BufferedImage srcImg, BufferedImage trgImg, double mut) {
//        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
//            int[] newColors = new int[4];
//            int total = 0;
//            for(int k = 1; k < ImgUtils.COLOR_CHANNEL_COUNT; k++){
//                total += channels[k];
//            }
//            total = new Double(total * 1.0 / 3.0).intValue();
//            newColors[0] = channels[0];
//            newColors[1] = total;
//            newColors[2] = total;
//            newColors[3] = total;
//            int newColor = ImgUtils.colorToRgb(newColors);
//            trgImg.setRGB(x, y, newColor);
//        });

        processGgbChannels(srcImg, trgImg, (gray) -> {
            double newDou = Math.log(gray / mut  + 1.0) / Math.log(255.0 / mut + 1.0) * 255;
            newDou = Math.log(gray * 1.0 / mut  + 1.0) / Math.log(255.0 / mut  + 1.0) * 255.0;

            int newGray = new Double(newDou).intValue();
            if(newGray > 255){
                newGray = 255;
            }else if(newGray < 0){
                newGray = 0;
            }
            return newGray;
        });
    }

    /**
     * 对数变换
     * @param srcImg 原图像
     * @param trgImg 目标图像
     * @param c 对数变换的系数
     */
    public static void logarithmTransformation(BufferedImage srcImg, BufferedImage trgImg, int c) {
        processGgbChannels(srcImg, trgImg, (gray) -> {
            int newGray = new Double( Math.log(gray * 1.0/255.0 + 1.0) * 255.0 * c).intValue();
            return newGray;
        });
    }

    /**
     * 对图像的rgb进行处理
     * @param srcImg 原图像
     * @param trgImg 目标图像
     * @param handler 处理接口
     */
    public static void processGgbChannels(BufferedImage srcImg, BufferedImage trgImg, ImageGrayHandler handler) {
        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
            int[] newColors = new int[4];
            for(int k = 1; k < ImgUtils.COLOR_CHANNEL_COUNT; k++){
                newColors[k] = handler.getNewGray(channels[k]);
            }
            newColors[0] = channels[0];
            int newColor = ImgUtils.colorToRgb(newColors);
            trgImg.setRGB(x, y, newColor);
        });
    }

    /**
     * 对图像进行黑白化
     * @param srcImg 原图像
     * @param trgImg 目标图像
     */
    public static void processBlackAndWhite(BufferedImage srcImg, BufferedImage trgImg) {
        ImgUtils.traversePngColor(srcImg, (x, y, color, channels) -> {
            int newColor = new Double((0.3 * channels[1] + 0.59 * channels[2] + 0.11 * channels[3])).intValue();
            channels[1] = newColor;
            channels[2] = newColor;
            channels[3] = newColor;
            newColor = ImgUtils.colorToRgb(channels);
            trgImg.setRGB(x, y, newColor);
        });
    }

    /**
     * 对图像进行黑白化
     * @param srcFilename 原图像
     * @param trgFilename 目标图像
     * @throws IOException 文件读取报错
     */
    public static void processBlackAndWhite(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        processBlackAndWhite(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * 计算图像的灰度值数组，灰度值=0.3r + 0.59g + 0.11b
     * @param img
     * @return
     */
    public static int[][] getImageGray(BufferedImage img){
        int[][] result = new int[img.getWidth()][img.getWidth()];
        ImgUtils.traversePngColor(img, (x, y, color, channels) -> {
            int newColor = new Double((0.3 * channels[1] + 0.59 * channels[2] + 0.11 * channels[3])).intValue();
            result[x][y] = newColor;
        });
        return result;
    }
}
