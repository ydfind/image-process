package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 算术均值滤波器
 * @author ydfind
 * @date 2019.9.27
 */
@Component
@Service
public class MeanFilterProcessor extends ImageProcessor {

    /**
     * 得到以像素为中心的九宫格
     * @param arr 数组
     * @param img 图像
     * @param x 当前位置x
     * @param y 当前位置y
     * @return 9宫格值
     */
    private static int[] getMedianArr(int[] arr, BufferedImage img, int x, int y){
        int w = img.getWidth() - 1;
        int h = img.getHeight() - 1;
        int k = 0;
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(x < 0 || x >= w || y < 0 || y >= h){
                    arr[k++] = -1;
                }else{
                    arr[k++] = img.getRGB(x, y);
                }
            }
        }
        return arr;
    }

    /**
     * 得到以像素为中心的九宫格
     * @param arr 数组
     * @param img 图像
     * @param x 当前位置x
     * @param y 当前位置y
     * @return 9宫格值
     */
    private static int[] calcMedianArr(BufferedImage img, int x, int y){
        int w = img.getWidth() - 1;
        int h = img.getHeight() - 1;
        int k = 0;
        int[] arr = new int[9];
        for(int i = x - 1; i <= x + 1; i++){
            for(int j = y - 1; j <= y + 1; j++){
                if(i < 0 || i >= w || j < 0 || j >= h){
                }else{
                    arr[k++] = img.getRGB(i, j);
                }
            }
        }
        int[] newArr = new int[k];
        for(int i = 0; i < k; i++){
            newArr[i] = arr[i];
        }
        return newArr;
    }

    /**
     * 算术均值滤波器
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg){
//        int[] colors = new int[9];
        int[][] rgbs = new int[9][4];
        for(int i = 0; i < srcImg.getWidth(); i++) {
            for (int j = 0; j < srcImg.getHeight(); j++) {
                int[] colors = calcMedianArr(srcImg, i, j);
                int count = colors.length;
                for (int k = 0; k < count; k++) {
                    rgbs[k] = ImgUtils.getChannelColor(colors[k]);
                }
                // rgb处理
                int[] channels = ImgUtils.getChannelColor(srcImg, i, j);
                for (int x = 1; x < 4; x++) {
                    int total = 0;
                    for (int k = 0; k < count; k++) {
                        total += rgbs[k][x];
                    }
                    channels[x] = new Double(total * 1.0 / count).intValue();
                }
                // 保存
                int color = ImgUtils.colorToRgb(channels);
                trgImg.setRGB(i, j, color);
            }
        }
    }

    /**
     * 算术均值滤波器
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void process(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = srcImg.getSubimage(0, 0, srcImg.getWidth(), srcImg.getHeight());
        process(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
