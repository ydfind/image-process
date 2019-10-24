package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 膨胀
 * @author ydfind
 * @date 2019.10.20
 */
@Component
@Service
public class ExpandProcessor extends ImageProcessor {

    private static final Integer OBJECT_GREY = 255;
    private static final Integer BACKGROUND_GREY = 0;

    /***************************************膨胀******************************************************/
    /**
     * 膨胀
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void expand(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        // 得到二值化图像图像
        int[][] pixels = getImageBinary(srcImg, 20);
        pixels = expand(pixels);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                curChannels[1] = pixels[i][j];
                curChannels[2] = pixels[i][j];
                curChannels[3] = pixels[i][j];
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * 对二值数组进行膨胀
     * @param mask 图像对应的二值数组
     * @return 膨胀后数组
     */
    public static int[][] expand(int[][] mask){
        int w = mask.length;
        int h = mask[0].length;
        int[][] res = new int[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                boolean bThereIsOneObjectPixel=false;
                for(int i = x - 1; i < x + 2; i++){
                    for(int j = y - 1; j < y + 2; j++){
                        if(i < 0 || i >= w || j < 0 || j >= h){
                            continue;
                        }
                        if (mask[i][j] == OBJECT_GREY){
                            bThereIsOneObjectPixel = true;
                        }
                    }
                }
                res[x][y] = bThereIsOneObjectPixel ? OBJECT_GREY : BACKGROUND_GREY;
            }
        }
        return res;
    }

    /**
     * 膨胀
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void expand(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        expand(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /*******************************************腐蚀******************************************************/
    /**
     * 腐蚀
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void erosion(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        // 得到二值化图像图像
        int[][] pixels = getImageBinary(srcImg, 20);
        pixels = erosion(pixels);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                curChannels[1] = pixels[i][j];
                curChannels[2] = pixels[i][j];
                curChannels[3] = pixels[i][j];
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * 对二值数组进行腐蚀
     * @param mask 图像对应的二值数组
     * @return 腐蚀后数组
     */
    public static int[][] erosion(int[][] mask){
        int w = mask.length;
        int h = mask[0].length;
        int[][] res = new int[w][h];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                boolean bThereIsOneObjectPixel=false;
                for(int i = x - 1; i < x + 2; i++){
                    for(int j = y - 1; j < y + 2; j++){
                        if(i < 0 || i >= w || j < 0 || j >= h){
                            continue;
                        }
                        if (mask[i][j] == BACKGROUND_GREY){
                            bThereIsOneObjectPixel = true;
                        }
                    }
                }
                res[x][y] = bThereIsOneObjectPixel ? BACKGROUND_GREY : OBJECT_GREY;
            }
        }
        return res;
    }

    /**
     * 腐蚀
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void erosion(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        erosion(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }
}
