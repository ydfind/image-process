package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.util.ImgUtils;

import java.awt.image.BufferedImage;

/**
 * 傅里叶计算公共类
 * @author ydfind
 * @date 2019.10.18
 */
public class FourierUtils {

    /**
     * 计算傅里叶的功率谱
     * @param complexes 傅里叶数组
     * @return 功率谱
     */
    public static double[][] calcPower(MyComplex[][] complexes){
        int w = complexes.length;
        int h = complexes[0].length;
        double[][] powers = new double[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                powers[i][j] = complexes[i][j].power();
            }
        }
        return powers;
    }

    /**
     * 计算总功率PT
     * @param complexes 傅里叶数组
     * @return 功率谱和
     */
    public static double sumPower(MyComplex[][] complexes){
        int w = complexes.length;
        int h = complexes[0].length;
        double[][] powers = calcPower(complexes);
        double total = 0;
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                total += powers[i][j];
            }
        }
        return total;
    }

    public static double calcPowerPercent(MyComplex[][] complexes, int radius){
        int w = complexes.length;
        int h = complexes[0].length;
        int cenX = w / 2;
        int cenY = h / 2;
        int maxLen = radius * radius;
        double power = 0;
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                int len =  new Double(Math.pow(x - cenX, 2.0) + Math.pow(y - cenY, 2.0)).intValue();
                if(len <= maxLen){
                    power += complexes[i][j].power();
                }
            }
        }
        double total = sumPower(complexes);
        double result = 100 * power / total;
        return result;
    }
}
