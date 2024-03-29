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
 * 椒盐噪声 service
 * @author ydfind
 */
@Component
@Service
public class PepperSaltNoiseProcessor extends ImageProcessor {

    public static final String PEPPER_SALT_NOISE_NAME = "椒盐噪声";

    public static final String PEPPER_NOISE_NAME = "胡椒噪声";

    public static final String SALT_NOISE_NAME = "盐噪声";

    /**
     * 添加椒盐噪声：根据信噪比，随机把点设置为0或1
     * @param srcImg 原图
     * @param trgImg 目标图像
     * @param param 信噪比
     * @param type 椒盐，椒，盐
     */
    public static void addPepperSaltNoise(BufferedImage srcImg, BufferedImage trgImg, double param, String type){
        int total = srcImg.getWidth() * srcImg.getHeight();
        int count = new Double(total * (1 - param)).intValue();
        Random random = new Random();
        for(int i = 0; i < count; i++) {
            int randomX = random.nextInt(srcImg.getWidth());
            int randomY = random.nextInt(srcImg.getHeight());
            int[] channels = ImgUtils.getChannelColor(srcImg, randomX, randomY);
            int newColor;
            if(PEPPER_SALT_NOISE_NAME.equals(type)){
                newColor = (random.nextInt(2) + 1) % 2 == 0 ? 0 : 255;
            }else if(PEPPER_NOISE_NAME.equals(type)){
                newColor = 0;
            }else{
                newColor = 255;
            }
            for(int k = ImgUtils.COLOR_CHANNEL_COUNT - 1; k > 0; k--){
                channels[k] = newColor;
            }
            int color = ImgUtils.colorToRgb(channels);
            trgImg.setRGB(randomX, randomY, color);

        }
    }

    /**
     * 添加椒盐噪声：根据信噪比，随机把点设置为0或1
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @param param 信噪比
     * @throws IOException 报错
     */
    public static void addPepperSaltNoise(String srcFilename, String trgFilename, double param) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        addPepperSaltNoise(srcImg, trgImg, param, PEPPER_SALT_NOISE_NAME);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

    /**
     * 添加椒盐噪声：根据信噪比，随机把点设置为0或1
     * @param srcFilename 原图
     * @param trgFilename 目标图像
     * @param param 信噪比
     * @param type 0椒盐，1椒，2盐
     * @throws IOException 报错
     */
    public static void addNoise(String srcFilename, String trgFilename, double param, String type) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        addPepperSaltNoise(srcImg, trgImg, param, type);
        ImgUtils.saveImage(trgFilename, trgImg);
    }
}
