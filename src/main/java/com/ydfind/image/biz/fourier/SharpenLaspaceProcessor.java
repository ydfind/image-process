package com.ydfind.image.biz.fourier;

import com.ydfind.image.biz.fft.MyComplex;

import java.io.IOException;

/**
 * 拉普拉斯 增强图像
 * @author ydfind
 * @date 2019.10.18
 */
public class SharpenLaspaceProcessor extends FourierProcessor {

    public static int[][] fourierProcessor(MyComplex[][] complexes){
        int w = complexes.length;
        int h = complexes[0].length;
        int[][] pixels = new int[w][h];
        int cenX = w / 2;
        int cenY = h / 2;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int x = i < cenX ? i + cenX : i - cenX;
                int y = j < cenY ? j + cenY : j - cenY;
                int len =  new Double(Math.pow(x - cenX, 2.0) + Math.pow(y - cenY, 2.0)).intValue();
                len = 1 - len;
                complexes[i][j] = complexes[i][j].times(len);
                int scope = new Double(complexes[i][j].abs()).intValue();
                pixels[x][y] = scope;
            }
        }
        rating(pixels);
        return pixels;
    }

    public static void process(String srcFilename, String trgFilename, String newSrcFilename) throws IOException {
        process(srcFilename, trgFilename, newSrcFilename, (complexes) -> fourierProcessor(complexes));
    }
}
