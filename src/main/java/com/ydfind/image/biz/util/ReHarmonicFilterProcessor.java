package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 逆谐波均值滤波
 * @author ydfind
 * @date 2019.10.20
 */
@Component
@Service
public class ReHarmonicFilterProcessor extends ImageProcessor {

    public static double pow(int a, double b){
        if(b == 0){
            return 1;
        }
        else if(a == 0){
            return 0;
        }else {
            return Math.pow(a, b);
        }
    }

    /**
     * 逆谐波均值滤波
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param q 0算术均值滤波;-1为谐波均值滤波；正,消除胡椒噪声;负，消除盐粒噪声
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg, double q) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        double q1 = q + 1;
        double q2 = q;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                double r = 0;
                double g = 0;
                double b = 0;
                double r1 = 0;
                double g1 = 0;
                double b1 = 0;
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if(x < 0 || x >= w || y < 0 || y >= h ){
                            continue;
                        }else {
                            int color = srcImg.getRGB(x, y);
                            int[] channels = ImgUtils.getChannelColor(color);
                            r += pow(channels[1], q1);
                            g += pow(channels[2], q1);
                            b += pow(channels[3], q1);
                            r1 += pow(channels[1], q2);
                            g1 += pow(channels[2], q2);
                            b1 += pow(channels[3], q2);
                        }
                    }
                }
                r = (r1 == 0) ? 0 : r / r1;
                g = (g1 == 0) ? 0 : g / g1;
                b = (b1 == 0) ? 0 : b / b1;
                curChannels[1] = new Double(r).intValue();
                curChannels[2] = new Double(g).intValue();
                curChannels[3] = new Double(b).intValue();
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * 算术均值滤波器
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @throws IOException 报错
     */
    public static void process(String srcFilename, String trgFilename, double q) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        process(srcImg, trgImg, q);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
