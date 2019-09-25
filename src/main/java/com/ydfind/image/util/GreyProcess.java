//package com.ydfind.image;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.file.Files;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//
///**
// * 图像灰度处理
// * @author ydfind
// */
//public class GreyProcess {
//
//    /**
//     * 图片灰度化的方法
//     * @param status 灰度化方法的种类，1表示最大值法，2表示最小值法，3表示均值法，4加权法
//     * @param imagePath 需要灰度化的图片的位置
//     * @param outPath 灰度化处理后生成的新的灰度图片的存放的位置
//     * @throws IOException
//     */
//    public static void grayImage(int status,String imagePath, String outPath) throws IOException {
//        File file = new File(imagePath);
//        BufferedImage image = ImageIO.read(file);
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        BufferedImage grayImage = new BufferedImage(width, height,  image.getType());
//        //BufferedImage grayImage = new BufferedImage(width, height,  BufferedImage.TYPE_BYTE_GRAY);
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int color = image.getRGB(i, j);
//                final int r = (color >> 16) & 0xff;
//                final int g = (color >> 8) & 0xff;
//                final int b = color & 0xff;
//                int gray=0;
//                if(status==1){
//                    //最大值法灰度化
//                    gray=getBigger(r, g, b);
//                }else if(status==2){
//                    //最小值法灰度化
//                    gray=getSmall(r, g, b);
//                }else if(status==3){
//
//                    gray=getAvg(r, g, b);
//                }else if(status==4){
//                    //加权法灰度化
//                    gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
//                }
//                System.out.println("像素坐标：" + " x=" + i + "   y=" + j + "   灰度值=" + gray);
//                grayImage.setRGB(i, j, colorToRgb(255, gray, gray, gray));
//            }
//        }
//        File newFile = new File(outPath);
//        ImageIO.write(grayImage, "jpg", newFile);
//    }
//
//    /**
//     * 比较三个数的大小
//     * @param x 参数1
//     * @param y 参数2
//     * @param z 参数3
//     * @return 返回3个参数的最大值
//     */
//    public static int getBigger(int x,int y,int z){
//        if(x>=y&&x>=z){
//            return x;
//        }else if(y>=x&&y>=z){
//            return y;
//        }else if(z>=x&&z>=y){
//            return z;
//        }else{
//            return 0;
//        }
//    }
//
//    /**
//     * 比较三个是的大小取最小数
//     * @param x 参数1
//     * @param y 参数2
//     * @param z 参数3
//     * @return 返回3个参数的最小值
//     */
//    public static int getSmall(int x,int y,int z){
//        if(x<=y&&x<=z){
//            return x;
//        }else if(y>=x&&y>=z){
//            return y;
//        }else if(z>=x&&z>=y){
//            return z;
//        }else{
//            return 0;
//        }
//    }
//
//    /**
//     * 均值法
//     * @param x 参数1
//     * @param y 参数2
//     * @param z 参数3
//     * @return 返回3个参数的平均值
//     */
//    public static int getAvg(int x,int y,int z){
//        int avg=(x+y+z)/3;
//        return avg;
//    }
//}
