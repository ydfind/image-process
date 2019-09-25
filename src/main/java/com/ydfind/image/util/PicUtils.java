//package com.ydfind.image.util;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
///**
// * 图像放大缩小
// * @author ydfind
// */
//public class PicUtils {
//    private String destFile;
//    private int width;
//    private int height;
//    private BufferedImage img;
//    private String ext;
//
//    /**
//     * 缩放图片工具的构造函数
//     * @param srcFile 图像带目录文件名
//     * @throws IOException 异常
//     */
//    public PicUtils(String srcFile) throws IOException {
//        //得到最后一个.的位置
//        int index = srcFile.lastIndexOf(".");
//        //获取被缩放的图片的格式
//        this.ext = srcFile.substring(index + 1);
//        //获取目标路径(和原始图片路径相同,在文件名后添加了一个_s)
//        this.destFile = srcFile.substring(0, index) + "_s." + ext;
//        //读取图片,返回一个BufferedImage对象
//        this.img = ImageIO.read(new File(srcFile));
//        //获取图片的长和宽
//        this.width = img.getWidth();
//        this.height = img.getHeight();
//    }
//
//    public PicUtils(String srcFile, String target) throws IOException {
//        //得到最后一个.的位置
//        int index = srcFile.lastIndexOf(".");
//        //获取被缩放的图片的格式
//        this.ext = srcFile.substring(index + 1);
//        //获取目标路径(和原始图片路径相同,在文件名后添加了一个_s)
//        this.destFile = target;
//        //读取图片,返回一个BufferedImage对象
//        this.img = ImageIO.read(new File(srcFile));
//        //获取图片的长和宽
//        this.width = img.getWidth();
//        this.height = img.getHeight();
//    }
//
//    /**
//     * 指定长和宽对图片进行缩放
//     * @param width 长
//     * @param height 宽
//     * @throws IOException
//     */
//    public void zoomBySize(int width, int height) throws IOException {
//        //与按比例缩放的不同只在于,不需要获取新的长和宽,其余相同.
//        Image imgTemp = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics = image.createGraphics();
//        graphics.drawImage(imgTemp, 0, 0, null);
//        graphics.dispose();
//        OutputStream out = new FileOutputStream(destFile);
//        ImageIO.write(image, ext, out);
//        out.close();
//    }
//
//}
