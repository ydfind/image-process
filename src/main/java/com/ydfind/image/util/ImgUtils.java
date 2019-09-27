package com.ydfind.image.util;

import com.ydfind.image.biz.ImageColorTraverse;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

/**
 * 图像工具类
 * @author ydfind
 */
@Slf4j
public class ImgUtils {

    /**
     * 数字2
     */
    public static final Integer NUM_2 = 2;

    public static final Integer COLOR_CHANNEL_COUNT = 4;

    public static final Integer PIXEL_MAX_VALUE = 256;

    /**
     * 比例缩放
     * @param source 原图像位置
     * @param target 目标图像
     * @param scale 缩放比例
     * @throws IOException 异常
     */
    public static void zoomByScale(String source, String target, double scale) throws IOException {
        BufferedImage img = ImageIO.read(new File(source));
        //获取图片的长和宽
        int h = img.getHeight();
        int w = img.getWidth();
        //获取缩放后的长和宽
        int wScale = (int) (scale * w);
        int hScale = (int) (scale * h);
        //获取缩放后的Image对象
        Image imgTemp = img.getScaledInstance(wScale, hScale, Image.SCALE_DEFAULT);
        //新建一个和Image对象相同大小的画布
        BufferedImage image = new BufferedImage(wScale, hScale, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D graphics = image.createGraphics();
        //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
        graphics.drawImage(imgTemp, 0, 0, null);
        //释放资源
        graphics.dispose();
        //使用ImageIO的方法进行输出,记得关闭资源
        OutputStream out = new FileOutputStream(target);
        int index = source.lastIndexOf(".");
        //获取被缩放的图片的格式
        String ext = source.substring(index + 1);
        ImageIO.write(image, ext, out);
        out.close();
    }

    /**
     * 灰度分辨率
     * @param source 原图像位置
     * @param target 目标图像
     * @param level 灰度分辨率等级
     * @throws IOException 异常
     */
    public static void adjustGray(String source, String target, int level) throws IOException {

        Files.deleteIfExists(new File(target).toPath());
        Files.copy(new File(source).toPath(), new File(target).toPath());

        File file = new File(target);
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = image.getRGB(i, j);
                final int el = (color >> 24) & 0xff;
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                image.setRGB(i, j, colorToRgb(el, getByGray(r, level), getByGray(g, level), getByGray(b, level)));
            }
        }

        saveImage(target, image);
    }

    /**
     *  颜色分量转换为RGB值
     * @param alpha 透明度
     * @param red 红
     * @param green 绿
     * @param blue 蓝
     * @return rgb值
     */
    public static int colorToRgb(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }

    /**
     *  颜色分量转换为RGB值
     * @param alpha 透明度
     * @param red 红
     * @param green 绿
     * @param blue 蓝
     * @return rgb值
     */
    public static int colorToRgb(int alpha, double red, double green, double blue) {
        return colorToRgb(alpha, new Double(red).intValue(), new Double(green).intValue(),
                new Double(blue).intValue());
    }

    /**
     *  颜色分量转换为RGB值
     * @param colors alpha、rgb颜色
     * @return rgb值
     */
    public static int colorToRgb(int[] colors) {
        return colorToRgb(colors[0], colors[1], colors[2], colors[3]);
//        return colorToRgb(colors[0], colors[3], colors[1], colors[2]);
    }

    /**
     * 将x进行灰度修改
     * @param x 值，0-255
     * @param level 灰度等级
     * @return 修改后的值
     */
    private static int getByGray(int x,int level){
        int item = 256 / level;
        int cur = x / item;
        if(cur > 0 && cur >= (level / NUM_2)) {
            cur = (cur + 1) * item - 1;
        }else{
            cur = cur * item;
        }
        return cur;
    }

    /**
     * 求最小值最大值
     * @param img 图像
     * @return 0位置为最小值，1位置为最大值
     */
    public static int[] getColorExtremeValues(BufferedImage img){
        int h = img.getHeight();
        int w = img.getWidth();
        int[] res = new int[2];
        res[0] = 256;
        res[1] = -1;
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int cl = img.getRGB(i, j);
                for(int k = 0; k < 3; k++){
                    int hue = cl & 0xff;
                    if(hue < res[0]){
                        res[0] = hue;
                    }
                    if(hue > res[1]){
                        res[1] = hue;
                    }
                    cl >>= 8;
                }
            }
        }
        return res;
    }

    /**
     * 求最小值最大值
     * @param img 图像
     * @return 0位置为最小值，1位置为最大值
     */
    public static int[][] getChannelExtremeValues(BufferedImage img){
        int[][] counts = new int[3][2];
        // 遍历统计
        ImgUtils.traversePngColor(img, (x, y, color, channels) -> {
            for(int i = 0; i < COLOR_CHANNEL_COUNT - 1; i++){
                if(channels[i + 1] < counts[i][0]){
                    counts[i][0] = channels[i + 1];
                }
                if(channels[i + 1] > counts[i][1]){
                    counts[i][1] = channels[i + 1];
                }
            }
        });
        return counts;
    }

    /**
     * 检查边界是否相等
     * @param srcStart 原图像开始
     * @param srcEnd 原图像结束
     * @param trgStart 原图像开始
     * @param trgEnd 原图像结束
     * @return 是否相等
     */
    public static boolean checkEqualEdge(int srcStart, int srcEnd, int trgStart, int trgEnd){
        if((srcStart == trgStart) && (srcEnd == trgEnd)){
            log.warn("source image edge equal target.{} = {}", srcStart, srcEnd);
            return true;
        }
        return false;
    }

    /**
     * 保存文件
     * @param trgFilename 目标文件
     * @param trgImg 要保存的图像
     * @throws IOException 写入异常
     */
    public static void saveImage(String trgFilename, BufferedImage trgImg) throws IOException {
        FileUtils.createFileIfNotExist(new File(trgFilename));
        //得到最后一个.的位置
        int index = trgFilename.lastIndexOf(".");
        //获取被缩放的图片的格式
        String ext = trgFilename.substring(index + 1);
        //使用ImageIO的方法进行输出,记得关闭资源
//        OutputStream out = new FileOutputStream(trgFilename);
        ImageIO.write(trgImg, ext, new File(trgFilename));
//        out.close();
    }

    /**
     * 图像遍历
     * @param img 图像
     * @param colorTraverse 遍历接口
     */
    public static void traversePngColor(BufferedImage img, ImageColorTraverse colorTraverse){
        for(int i = 0; i < img.getWidth(); i++){
            for(int j = 0; j < img.getHeight(); j++){
                int color = img.getRGB(i, j);
                int[] channels = new int[4];
                for(int k = ImgUtils.COLOR_CHANNEL_COUNT - 1; k >= 0; k--){
                    channels[k] = color & 0xff;
                    color >>= 8;
                }
                colorTraverse.traverse(i, j, color, channels);
            }
        }
    }

    /**
     * 统计图像像素rgb的灰度数量
     * @param img 统计的图像
     * @return int[3][256]，int[0]为对r统计，后面对gb统计
     */
    public static double[][] statColorChannels(BufferedImage img){
        double[][] counts = new double[3][256];
        // 遍历统计
        ImgUtils.traversePngColor(img, (x, y, color, channels) -> {
            for(int i = 0; i < COLOR_CHANNEL_COUNT - 1; i++){
                counts[i][channels[i + 1]] += 1;
            }
        });
        return counts;
    }

    /**
     * 得到文件名的格式名
     * @param path 文件名或带路径文件名
     * @return 文件格式
     */
    public static String getImgExt(String path){
        // 得到最后一个.的位置
        int index = path.lastIndexOf(".");
        // 文件格式
        String ext = path.substring(index + 1);
        return ext;
    }

    /**
     * 得到不带格式的文件名称
     * @param path 文件名或带路径文件名
     * @return 文件名
     */
    public static String getImgNameExcludeExt(String path){
        // 得到最后一个.的位置
        int index = path.lastIndexOf(".");
        // 文件格式
        String excludeExt = path.substring(0, index);
        index = excludeExt.lastIndexOf("/");
        int index1 = excludeExt.lastIndexOf("\\");
        if(index1 > index){
            index = index1;
        }
        String name = excludeExt.substring(index + 1);
        return name;
    }

    public static void main(String[] args){
        String path = "/update/image.exe";
        System.out.println(getImgNameExcludeExt(path));
    }
}
