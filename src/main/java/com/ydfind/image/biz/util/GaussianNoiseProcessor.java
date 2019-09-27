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

    public static double generateGaussianNoise() {
//        double V1, V2, S;
//        double X;
//        double U1,U2;
//        Random random = new Random();
//        do {
//            U1 = (double)random.nextInt(256);
//            U2 = (double)random.nextInt(256);
//
//            V1 = 2 * U1 - 1;
//            V2 = 2 * U2 - 1;
//            S = V1 * V1 + V2 * V2;
//        } while(S >= 1 || S == 0);
//
//        X = V1 * Math.sqrt(-2 * Math.log(S) / S);
//        return X;


        double u1, u2;
        Random random = new Random();
        do {
            u1 = (double)random.nextInt(256) / 256;
            u2 = (double)random.nextInt(256) / 256;
//            u1 = rand() * (1.0 / RAND_MAX);
//            u2 = rand() * (1.0 / RAND_MAX);
        } while (u1 <= Double.MIN_NORMAL);
        //flag为真构造高斯随机变量x
//        double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2 * CV_PI * u2);
//        z1 = sqrt(-2.0 * log(u1)) * sin(2 * CV_PI * u2);
//        return z0;
        return 1.0;
    }



    /**
     * 添加高斯噪声：根据信噪比，随机把点设置为0或1
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param param 信噪比
     */
    public static void addGaussianNoise(BufferedImage srcImg, BufferedImage trgImg, double param){
        for(int x = 0; x < srcImg.getWidth(); x++){
            for(int y= 0; y < srcImg.getHeight(); y++){
                double k1 = param * generateGaussianNoise();
                int[] channels = ImgUtils.getChannelColor(srcImg, x, y);
                for(int k = ImgUtils.COLOR_CHANNEL_COUNT - 1; k > 0; k--){
                    channels[k] += new Double(k1).intValue();
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
