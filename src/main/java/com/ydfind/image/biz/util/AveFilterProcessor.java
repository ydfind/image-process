package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 平均滤波器
 * @author ydfind
 * @date 2019.9.27
 */
@Component
@Service
public class AveFilterProcessor extends ImageProcessor {

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
                arr[k++] = img.getRGB(getInRange(x + i, 0, w),getInRange( y + j,0, h ));
            }
        }
        return arr;
    }

    /**
     * 求x在min-max范围内的值
     * @param x 原值
     * @param min 最小值
     * @param max 最大值
     * @return 返回符合条件的值
     */
    private static int getInRange(int x, int min, int max){
        x = x < min ? min : (x > max) ? max : x;
        return x;
    }

    /**
     * 中值滤波器
     * @param srcImg 原图
     * @param trgImg 目标图像
     */
    public static void process(BufferedImage srcImg, BufferedImage trgImg){

        int[] colors = new int[9];
        int[][] rgbs = new int[9][4];
        for(int i = 0; i < srcImg.getWidth(); i++) {
            for (int j = 0; j < srcImg.getHeight(); j++) {
                int[] channels = ImgUtils.getChannelColor(srcImg, i, j);
                colors = getMedianArr(colors, srcImg, i, j);
                for (int k = 0; k < colors.length; k++) {
                    rgbs[k] = ImgUtils.getChannelColor(colors[k]);
                }
                // rgb处理
                for (int x = 1; x < 4; x++) {
                    int total = 0;
                    for (int k = 0; k < colors.length; k++) {
                        total += rgbs[k][x];
                    }
                    channels[x] = total / 9;
                }
                // 保存
                int color = ImgUtils.colorToRgb(channels);
                trgImg.setRGB(i, j, color);
            }
        }
    }

    /**
     * 中值滤波器
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
