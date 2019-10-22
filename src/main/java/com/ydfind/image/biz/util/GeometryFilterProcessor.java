package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 几何均值滤波
 * @author ydfind
 * @date 2019.10.20
 */
@Component
@Service
public class GeometryFilterProcessor extends ImageProcessor {

    /**
     * 几何均值滤波
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] curChannels = ImgUtils.getChannelColor(srcImg.getRGB(i, j));
                double r = 1.0;
                double g = 1.0;
                double b = 1.0;
                int count = 0;
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if(x < 0 || x >= w || y < 0 || y >= h ){
                            continue;
                        }else {
                            int color = srcImg.getRGB(x, y);
                            int[] channels = ImgUtils.getChannelColor(color);
                            r *= channels[1] + 1;
                            g *= channels[2] + 1;
                            b *= channels[3] + 1;
                            count++;
                        }
                    }
                }
                curChannels[1] = new Double(Math.pow(r, 1.0/count)).intValue();
                curChannels[2] = new Double(Math.pow(g, 1.0/count)).intValue();
                curChannels[3] = new Double(Math.pow(b, 1.0/count)).intValue();
                trgImg.setRGB(i, j, ImgUtils.colorToRgb(curChannels));
            }
        }
    }

    /**
     * 几何均值滤波
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
