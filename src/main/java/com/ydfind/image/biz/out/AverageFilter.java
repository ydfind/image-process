package com.ydfind.image.biz.out;

import com.ydfind.image.biz.util.ReHarmonicFilterProcessor;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import com.ydfind.image.util.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 几种均值滤波算法实现 1、算术均值滤波 2、几何均值滤波 3、谐波均值滤波 4、逆谐波均值滤波
 *
 * @author admin
 */
public class AverageFilter {
    // singleton
    private static AverageFilter averageFilter = new AverageFilter();

    public static AverageFilter getInstance() {
        return averageFilter;
    }

    private AverageFilter() {
    }

    /**
     * 算术均值滤波 滤波器大小：param取值3、5、7、9...(2n+1 ) 产生一幅模糊图像
     *
     * @param image
     */
    public void arithmeticAverageFilter(BufferedImage image, int param) {
        // 创建一个临时图像，为了保证原图边缘参与计算，临时图像比原图大param-1
        BufferedImage tempImage = new BufferedImage(image.getWidth() + param - 1, image.getHeight() + param - 1,
                image.getType());
        // 对图像进行填充，边缘像素采用最近像素填充
        nearFillEdge(tempImage, image, param);
        // 进行卷积运算
        for (int i = (param - 1) / 2; i < tempImage.getWidth() - (param - 1) / 2; i++) {
            for (int j = (param - 1) / 2; j < tempImage.getHeight() - (param - 1) / 2; j++) {
                int r = 0, g = 0, b = 0;
                // 计算滤波器内所有像素，R、G、B各个分量总和
                for (int x = -(param - 1) / 2; x <= (param - 1) / 2; x++) {
                    for (int y = -(param - 1) / 2; y <= (param - 1) / 2; y++) {
                        int tempRGB = tempImage.getRGB(i + x, j + y);
                        int tempR = (tempRGB >> 16) & 0xff;
                        int tempG = (tempRGB >> 8) & 0xff;
                        int tempB = tempRGB & 0xff;
                        r += tempR;
                        g += tempG;
                        b += tempB;
                    }
                }
                // 采用总和除以滤波器内总像素数量得到均值
                r = (int) (r / Math.pow(param, 2));
                g = (int) (g / Math.pow(param, 2));
                b = (int) (b / Math.pow(param, 2));
                int rgb = (255 & 0xff) << 24 | (clamp(r) & 0xff) << 16 | (clamp(g) & 0xff) << 8 | (clamp(b) & 0xff);
                image.setRGB(i - (param - 1) / 2, j - (param - 1) / 2, rgb);
            }
        }
    }

    /**
     * 几何均值滤波
     *
     * @param image
     * @param param
     */
    public void geometryAverageFilter(BufferedImage image, int param) {
        // 创建临时图像
        BufferedImage tempImage = new BufferedImage(image.getWidth() + param - 1, image.getHeight() + param - 1,
                image.getType());
        // 填充边缘
        nearFillEdge(tempImage, image, param);
        // 进行卷积运算
        for (int i = (param - 1) / 2; i < tempImage.getWidth() - (param - 1) / 2; i++) {
            for (int j = (param - 1) / 2; j < tempImage.getHeight() - (param - 1) / 2; j++) {
                double r = 1.0, g = 1.0, b = 1.0;
                for (int x = -(param - 1) / 2; x <= (param - 1) / 2; x++) {
                    for (int y = -(param - 1) / 2; y <= (param - 1) / 2; y++) {
                        int tempRGB = tempImage.getRGB(i + x, j + y);
                        double tempR = (tempRGB >> 16) & 0xff;
                        double tempG = (tempRGB >> 8) & 0xff;
                        double tempB = tempRGB & 0xff;
                        r *= Math.pow(tempR + 1, 1.0 / (param * param));
                        g *= Math.pow(tempG + 1, 1.0 / (param * param));
                        b *= Math.pow(tempB + 1, 1.0 / (param * param));
                    }
                }
                int rgb = (255 & 0xff) << 24 | (clamp((int) r) & 0xff) << 16 | (clamp((int) g) & 0xff) << 8
                        | (clamp((int) b) & 0xff);
                image.setRGB(i - (param - 1) / 2, j - (param - 1) / 2, rgb);
            }
        }
    }

    /**
     * 谐波均值滤波
     *
     * @param image
     * @param param
     */
    public void harmonicFilter(BufferedImage image, int param) {
        // 创建temp图像
        BufferedImage tempImage = new BufferedImage(image.getWidth() + param - 1, image.getHeight() + param - 1,
                image.getType());
        // 填充边缘
        nearFillEdge(tempImage, image, param);
        // 进行卷积运算
        for (int i = (param - 1) / 2; i < tempImage.getWidth() - (param - 1) / 2; i++) {
            for (int j = (param - 1) / 2; j < tempImage.getHeight() - (param - 1) / 2; j++) {
                double r = 0, g = 0, b = 0;
                for (int x = -(param - 1) / 2; x <= (param - 1) / 2; x++) {
                    for (int y = -(param - 1) / 2; y <= (param - 1) / 2; y++) {
                        int tempRGB = tempImage.getRGB(i + x, j + y);
                        double tempR = (tempRGB >> 16) & 0xff;
                        double tempG = (tempRGB >> 8) & 0xff;
                        double tempB = tempRGB & 0xff;
                        r += 1 / tempR;
                        g += 1 / tempG;
                        b += 1 / tempB;
                    }
                }
                r = param * param / r;
                g = param * param / g;
                b = param * param / b;
                int rgb = (255 & 0xff) << 24 | (clamp((int) r) & 0xff) << 16 | (clamp((int) g) & 0xff) << 8
                        | (clamp((int) b) & 0xff);
                image.setRGB(i - (param - 1) / 2, j - (param - 1) / 2, rgb);
            }
        }
    }

    /**
     * 逆谐波均值滤波
     *
     * @param image
     * @param param
     * @param Q
     *            当Q=0为算术均值滤波，Q=-1为谐波均值滤波；Q为正，消除胡椒噪声，Q为负，消除盐粒噪声
     */
    public void reverseHarmonicFilter(BufferedImage image, int param, int Q) {
        // 创建temp图像
        BufferedImage tempImage = new BufferedImage(image.getWidth() + param - 1, image.getHeight() + param - 1,
                image.getType());
        // 填充边缘
        nearFillEdge(tempImage, image, param);
        // 进行卷积运算
        int param1 = (param - 1) / 2;
        for (int i = param1; i < tempImage.getWidth() - param1; i++) {
            for (int j = param1; j < tempImage.getHeight() - param1; j++) {
                double r = 0, g = 0, b = 0, r1 = 0, g1 = 0, b1 = 0;
                for (int x = i - param1; x <= i + param1; x++) {
                    for (int y = j - param1; y <= j + param1; y++) {
                        int tempRGB = tempImage.getRGB(x, y);
                        double tempR = (tempRGB >> 16) & 0xff;
                        double tempG = (tempRGB >> 8) & 0xff;
                        double tempB = tempRGB & 0xff;
                        r += Math.pow(tempR, Q + 1);
                        g += Math.pow(tempG, Q + 1);
                        b += Math.pow(tempB, Q + 1);
                        r1 += Math.pow(tempR, Q);
                        g1 += Math.pow(tempG, Q);
                        b1 += Math.pow(tempB, Q);
                    }
                }
                r = r / r1;
                g = g / g1;
                b = b / b1;
                int rgb = (255 & 0xff) << 24 | (clamp((int) r) & 0xff) << 16 | (clamp((int) g) & 0xff) << 8
                        | (clamp((int) b) & 0xff);
                image.setRGB(i - param1, j - param1, rgb);
            }
        }

    }

    // 判断r,g,b值，大于256返回256，小于0则返回0,0到256之间则直接返回原始值
    private int clamp(int rgb) {
        if (rgb > 255)
            return 255;
        if (rgb < 0)
            return 0;
        return rgb;
    }

    // 填充图像边缘空白像素,使用最近像素填充
    private void nearFillEdge(BufferedImage tempImage, BufferedImage image, int param) {
        for (int i = 0; i < tempImage.getWidth(); i++) {
            for (int j = 0; j < tempImage.getHeight(); j++) {
                // 临时图像位置没超过原图第一个位置,左下角
                if (i <= (param - 1) / 2 & j <= (param - 1) / 2) {
                    int rgb = image.getRGB(0, 0);
                    tempImage.setRGB(i, j, rgb);
                }
                // 临时图像位置超过横坐标最大，小于纵坐标最小，右下角
                if (i >= tempImage.getWidth() - (param - 1) / 2 - 1 & j <= (param - 1) / 2) {
                    int rgb = image.getRGB(image.getWidth() - 1, 0);
                    tempImage.setRGB(i, j, rgb);
                }
                // 临时图像位置超过纵坐标最大，小于横坐标最小，左上角
                if (j >= tempImage.getHeight() - (param - 1) / 2 - 1 & i <= (param - 1) / 2) {
                    int rgb = image.getRGB(0, image.getHeight() - 1);
                    tempImage.setRGB(i, j, rgb);
                }
                // 临时图像位置横纵坐标都超过原图最大位置，右上角
                if (i >= tempImage.getWidth() - (param - 1) / 2 - 1
                        & j >= tempImage.getHeight() - (param - 1) / 2 - 1) {
                    int rgb = image.getRGB(image.getWidth() - 1, image.getHeight() - 1);
                    tempImage.setRGB(i, j, rgb);
                }
                // 临时图像位置横坐标大于最小，小于最大，纵坐标小于最小，正下方
                if (i > (param - 1) / 2 & i < tempImage.getWidth() - (param - 1) / 2 - 1 & j <= (param - 1) / 2) {
                    int rgb = image.getRGB(i - (param - 1) / 2, 0);
                    tempImage.setRGB(i, j, rgb);
                }
                // 临时图像位置横坐标小于最小，纵坐标大于最小，小于最大，左边
                if (j > (param - 1) / 2 & j < tempImage.getHeight() - (param - 1) / 2 - 1 & i <= (param - 1) / 2) {
                    int rgb = image.getRGB(0, j - (param - 1) / 2);
                    tempImage.setRGB(i, j, rgb);
                }
                // 右边
                if (j > (param - 1) / 2 & j < tempImage.getHeight() - (param - 1) / 2 - 1
                        & i >= tempImage.getWidth() - (param - 1) / 2 - 1) {
                    int rgb = image.getRGB(image.getWidth() - 1, j - (param - 1) / 2);
                    tempImage.setRGB(i, j, rgb);
                }
                // 上方
                if (i > (param - 1) / 2 & i < tempImage.getWidth() - (param - 1) / 2 - 1
                        & j >= tempImage.getHeight() - (param - 1) / 2 - 1) {
                    int rgb = image.getRGB(i - (param - 1) / 2, image.getHeight() - 1);
                    tempImage.setRGB(i, j, rgb);
                }
                // 中间
                if (i > (param - 1) / 2 & i < tempImage.getWidth() - (param - 1) / 2 - 1 & j > (param - 1) / 2
                        & j < tempImage.getHeight() - (param - 1) / 2 - 1) {
                    int rgb = image.getRGB(i - (param - 1) / 2, j - (param - 1) / 2);
                    tempImage.setRGB(i, j, rgb);
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        test1(1.5);
        test1(-1.5);
    }

    public static void test1(double param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/pepper512x512.png";
        src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512-pepper-0.9.png";
        src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512-guassian-32.png";
        String trg = FileUtils.suffixToFilename(src, "-harmonic-" + param);

        BufferedImage srcImg = ImageIO.read(new File(src));
        BufferedImage trgImg = ImgUtils.copyImage(srcImg);
        AverageFilter averageFilter = new AverageFilter();
        averageFilter.harmonicFilter(trgImg, 3);
        ImgUtils.saveImage(trg, trgImg);
    }
}
