package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * HSI模式
 * @author ydfind
 * @date 2019.10.20
 */
@Component
@Service
public class HsiProcessor extends ImageProcessor {

    /**
     * HSI转为RGB模式
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void hsiToRgb(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                double hsiH = curChannels[1] * 2 * Math.PI / 255.0;
                double hsiS = curChannels[2] / 255.0;
                double hsiI = curChannels[3] / 255.0;
                double b = 0;
                double r = 0;
                double g = 0;
                double temp = hsiI * (1 - hsiS);
                if (hsiH >= 0 && hsiH < (Math.PI * 2 / 3)) {
                    b = temp;
                    r = hsiI * (1 + hsiS * Math.cos(hsiH) / Math.cos(Math.PI / 3.0 - hsiH));
                    g = 3 * hsiI - (b + r);
                }else if (hsiH >= (Math.PI * 2 / 3) && hsiH < (Math.PI * 4 / 3)){
                    r = temp;
                    g = hsiI * (1 + hsiS * Math.cos(hsiH - Math.PI * 2.0 / 3.0) / Math.cos(Math.PI - hsiH));
                    b = 3 * hsiI - (r + g);
                }else if (hsiH >= (Math.PI * 4 / 3) && hsiH <= (Math.PI * 2)){
                    g = temp;
                    b = hsiI * (1 + hsiS * Math.cos(hsiH - Math.PI * 2.0 / 3.0) / Math.cos(Math.PI * 5.0 / 3.0 - hsiH));
                    r = 3 * hsiI - (g + b);
                }
                curChannels[1] = new Double(r * 255).intValue();
                curChannels[2] = new Double(g * 255).intValue();
                curChannels[3] = new Double(b * 255).intValue();
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * RGB转为HSI模式
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void rgbToHsi(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                double r = curChannels[1];
                double g = curChannels[2];
                double b = curChannels[3];

                r = r / 255.0; g = g / 255.0; b = b / 255.0;
                double hsiI = 0;
                double hsiS = 0;
                double hsiH = 0;
                double total = r + g + b;
                hsiI = total / 3.0;
                if(hsiI < 0 || (curChannels[1] == curChannels[2] && curChannels[2] == curChannels[3])){
                    hsiI = 0;
                }else {
                    if(hsiI > 1){
                        hsiI = 1;
                    }
                    double min = Math.min(Math.min(r, g), b);
                    hsiS = 1 - 3.0 * min / total;
                    hsiH = Math.acos((2 * r - g - b) / 2.0 / Math.pow(Math.pow(r - g, 2) + (r - b) * (g - b), 0.5));
                    if(g < b){
                        hsiH = 2 * Math.PI - hsiH;
                    }
                    hsiH = hsiH / (2 * Math.PI);
                    hsiH *= 255;
                    hsiI *= 255;
                    hsiS *= 255;
                }
                curChannels[1] = new Double(hsiH).intValue();
                curChannels[2] = new Double(hsiS).intValue();
                curChannels[3] = new Double(hsiI).intValue();
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * RGB转为HSI模式
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void rgbToHsi(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        rgbToHsi(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * HSI转为RGB模式
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void hsiToRgb(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        hsiToRgb(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /***************************************hsi提取红色******************************************************/
    /**
     * HSI提取红色
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void hsiToRed(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                double hsiH = curChannels[1] * 2 * Math.PI / 255.0;
                double hsiS = curChannels[2] / 255.0;
                double hsiI = curChannels[3] / 255.0;
                double r = 0;
                double temp = hsiI * (1 - hsiS);
                if (hsiH >= 0 && hsiH < (Math.PI * 2 / 3)) {
                    r = hsiI * (1 + hsiS * Math.cos(hsiH) / Math.cos(Math.PI / 3.0 - hsiH));
                }else if (hsiH >= (Math.PI * 2 / 3) && hsiH < (Math.PI * 4 / 3)){
                    r = temp;
                }else if (hsiH >= (Math.PI * 4 / 3) && hsiH <= (Math.PI * 2)){
                    double g = temp;
                    double b = hsiI * (1 + hsiS * Math.cos(hsiH - Math.PI * 2.0 / 3.0) / Math.cos(Math.PI * 5.0 / 3.0 - hsiH));
                    r = 3 * hsiI - (g + b);
                }
                curChannels[1] = new Double(r * 255).intValue();
                curChannels[2] = 0;
                curChannels[3] = 0;
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * HSI提取红色
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void hsiToRed(String srcFilename, String trgFilename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        hsiToRed(srcImg, trgImg);
        ImgUtils.saveImage(trgFilename, trgImg);
    }
}
