package com.ydfind.image.biz.util;

import com.ydfind.image.util.ImgUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 中值滤波器
 * @author ydfind
 * @date 2019.9.27
 */
@Component
@Service
public class MedianFilterProcessor extends ImageProcessor {

    /**
     * 快速排序
     * @param arr 数组
     * @param low 开始位置
     * @param high 结束位置
     */
    public static void quickSort(int[] arr,int low,int high){
        if(low > high){
            return;
        }
        int i = low;
        int j = high;
        int temp = arr[low];

        while (i < j) {
            while (temp <= arr[j] && i < j ){
                j--;
            }
            while (temp >= arr[i] && i < j){
                i++;
            }
            if (i < j) {
                int t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        arr[low] = arr[i];
        arr[i] = temp;
        quickSort(arr, low, j - 1);
        quickSort(arr, j + 1, high);
    }

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
                if(x < 0 || x >= w || y < 0 || y >= h){
                    arr[k++] = -1;
                }else{
                    arr[k++] = img.getRGB(x, y);
                }
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
                    int count = 0;
                    for (int k = 0; k < colors.length; k++) {
//                        colors[k] = rgbs[k][x];
                        if(rgbs[k][x] >= 0) {
                            count++;
                        }
                    }
                    quickSort(colors, colors.length - count, colors.length - 1);
                    channels[x] = colors[colors.length - count + count / 2];
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
