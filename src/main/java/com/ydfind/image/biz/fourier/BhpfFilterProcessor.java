package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 傅里叶 巴特沃思高通滤波器
 * @author ydfind
 * @date 2019.10.18
 */
public class BhpfFilterProcessor extends FourierProcessor {

    private static int[][] lowFilter(MyComplex[][] complexes, int radius, int n, Double a, Double b){
        int w = complexes.length;
        int h = complexes[0].length;
        int[][] pixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                double len = Math.hypot(x - cenX, y - cenY);
                double hLow = 0;
                if(len > 0) {
                    hLow = 1 / (1 + Math.pow(radius / len, 2.0 * n));
                }
                if(Objects.nonNull(a)){
                    hLow = a + b * hLow;
                }
                complexes[i][j] = complexes[i][j].times(hLow);
                int scope = new Double(complexes[i][j].abs()).intValue();
                // 显示更明显的图像
                scope = scope * 1000;
                pixels[x][y] = scope;
            }
        }
        return pixels;
    }

    public static void process(String srcFilename, String scopeFilename, String trgFilename,
                               int radius, int n) throws IOException {
        process(srcFilename, scopeFilename, trgFilename, (complexes) -> lowFilter(complexes, radius, n, null, null));
    }

    public static void process(String srcFilename, String scopeFilename, String trgFilename,
                               int radius, int n, double a, double b) throws IOException {
        process(srcFilename, scopeFilename, trgFilename, (complexes) -> lowFilter(complexes, radius, n, a, b));
    }
}
