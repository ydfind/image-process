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

    /**
     * 逆谐波均值滤波
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param q 0算术均值滤波;-1为谐波均值滤波；正,消除胡椒噪声;负，消除盐粒噪声
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg, int q) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
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
                            r += Math.pow(channels[1], q + 1);
                            g += Math.pow(channels[2], q + 1);
                            b += Math.pow(channels[3], q + 1);
                            r1 += Math.pow(channels[1], q);
                            g1 += Math.pow(channels[2], q);
                            b1 += Math.pow(channels[3], q);
                        }
                    }
                }
                r = r1 < 1 ? 0 : r / r1;
                g = g1 < 1 ? 0 : g / g1;
                b = b1 < 1 ? 0 : b / b1;
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
    public static void process(String srcFilename, String trgFilename, int q) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = srcImg.getSubimage(0, 0, srcImg.getWidth(), srcImg.getHeight());
        process(srcImg, trgImg, q);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
