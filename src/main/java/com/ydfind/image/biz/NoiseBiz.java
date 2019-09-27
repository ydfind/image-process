package com.ydfind.image.biz;

import com.ydfind.image.service.ImageService;
import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像 service
 * @author ydfind
 */
@Component
@Service
public class NoiseBiz extends ImageBaseBiz{

    /**
     * 椒盐噪声处理
     * @param srcImg 原图像
     * @param trgImg 处理后保存图像
     * @param handler 处理函数
     */
    public static void pepperSaltNoise(BufferedImage srcImg, BufferedImage trgImg, ImageGrayHandler handler) {
        processGgbChannels(srcImg, trgImg, handler);
    }

    /**
     * 椒盐噪声处理
     * @param srcFilename 原图像
     * @param trgFilename 处理后保存图像
     * @param handler 处理函数
     */
    public static void pepperSaltNoise(String srcFilename, String trgFilename,
                                              ImageGrayHandler handler) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(srcFilename));
        BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        grayLineTransformation(srcImg, trgImg, handler);
        ImgUtils.saveImage(trgFilename, trgImg);
    }

}
