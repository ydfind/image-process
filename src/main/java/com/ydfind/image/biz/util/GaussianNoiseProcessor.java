package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 高斯噪声 service
 * @author ydfind
 */
@Component
@Service
public class GaussianNoiseProcessor extends ImageProcessor {

    /**
     *
     * @param flag 第一次调用应该为false
     * @param z1
     * @return
     */
    public static double generateGaussianNoise(Random random, Boolean flag, Double z1) {
        flag = !flag;
        if (!flag) {
            return z1;
        }
        double z0;
        double u1;
        double u2;
        do
        {
            u1 = random.nextFloat();
            u2 = random.nextFloat();
        } while (u1 <= Double.MIN_VALUE);
        z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        z1 = Math.sqrt(-2.0 * Math.log(u1))* Math.sin(2 * Math.PI * u2);
        return z0;
    }



    /**
     * 添加高斯噪声：根据信噪比，随机把点设置为0或1
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param param 信噪比
     */
    public static void addGaussianNoise(BufferedImage srcImg, BufferedImage trgImg, double param){
        Boolean flag = false;
        Double z1 = 0.0;
        double z0;
        Random random = new Random();
        for(int x = 0; x < srcImg.getWidth(); x++){
            for(int y= 0; y < srcImg.getHeight(); y++){
                z0 = generateGaussianNoise(random, flag, z1);
                int[] channels = ImgUtils.getChannelColor(srcImg, x, y);
                for(int k = ImgUtils.COLOR_CHANNEL_COUNT - 1; k > 0; k--){
                    channels[k] += new Double(z0 * param).intValue();
                    channels[k] = channels[k] > 255 ? 255 : channels[k] < 0 ? 0 : channels[k];
                }
                int color = ImgUtils.colorToRgb(channels);
                trgImg.setRGB(x, y, color);
            }
        }
    }

    /**
     * 添加高斯噪声：根据信噪比，随机把点设置为0或1
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @param param 信噪比
     * @throws IOException 报错
     */
    public static void addGaussianNoise(String srcFilename, String trgFilename, double param) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = srcImg.getSubimage(0, 0, srcImg.getWidth(), srcImg.getHeight());
        addGaussianNoise(srcImg, trgImg, param);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
