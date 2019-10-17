package com.ydfind.image.biz.fft;

import com.ydfind.image.common.Constant;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FourierTransformer  extends JFrame{
    Image im;
    BufferedImage imageAuth = null;
    int iw;
    int ih;
    int[] pixels;
    int[] newPixels;

    private static String sourceSrc = Constant.TEST_IMAGE_DIR + "old64x64.bmp";
    private static String sourceDesc = Constant.TEST_IMAGE_DIR + "old64x64-result.bmp";

    public FourierTransformer() {

        try {
//            String src = "D:\\文档\\图像处理\\图像库\\05-3-1.BMP";
//            this.im = ImageIO.read(new File(src));
//            this.im = ImageIO.read(getClass().getResource(src));
            this.im = ImageIO.read(new File(sourceSrc));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        this.iw = im.getWidth(null);
        this.ih = im.getHeight(null);
        pixels = new int[iw * ih];
        try {
            PixelGrabber pg = new PixelGrabber(im, 0, 0, iw, ih, pixels, 0, iw);
            pg.grabPixels();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(this.im, 0, 100, this.iw, this.ih, this);
        if(imageAuth != null) {
            g.drawImage(imageAuth, 250, 100, imageAuth.getWidth(), imageAuth.getHeight(), this);
        }

    }

    public static void main(String[] args){
        FourierTransformer frame = new FourierTransformer();

        frame.setSize(600, 500);
        frame.setTitle("Image");
        frame.setName("hello world");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        frame.setVisible(true);
        frame.convert(frame.getGraphics());
    }

    public Image convert(Graphics g) {
        // 赋初值
        int w = 1;
        int h = 1;
        // 计算进行付立叶变换的宽度和高度（2的整数次方）
        while (w * 2 <= iw) {
            w *= 2;
        }
        while (h * 2 <= ih) {
            h *= 2;
        }
        // 分配内存
        MyComplex[] src = new MyComplex[h * w];
        MyComplex[] dest = new MyComplex[h * w];
        newPixels = new int[h * w];
        // 初始化newPixels
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // drd: 为什么只要前8位？？？w >=iw,超出pixel范围，怎么办？？？
                newPixels[i * w + j] = pixels[i * iw + j] & 0xff;
            }
        }
        // 初始化src,dest
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                dest[i * w + j] = new MyComplex();
                src[i * w + j] = new MyComplex(newPixels[i * w + j], 0);
            }
        }
        // 在y方向上进行快速傅立叶变换
        for (int i = 0; i < h; i++) {
            FFT.fft(src, i, w, dest);
        }
        /**
         * 以下一定要进行转换，高手指点一下原因 (^ - ^)
         */
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                src[j * h + i] = dest[i * w + j];
//				System.out.println("dest " + j*h+i + ",  src " + i*w+j);
            }
        }
        // 对x方向进行傅立叶变换
        for (int i = 0; i < w; i++) {
            FFT.fft(src, i, h, dest);
        }
        /**
         * 将图像看做二维函数，图像灰度值为函数在相应XY处的函数值，对其进行二维快速傅里叶变换，
         * 得到一个复数矩阵，将此矩阵水平循环移动半宽，垂直循环移动半高。
         */
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                double re = dest[j * h + i].getReal();
                double im = dest[j * h + i].getImaginary();
                int ii = 0, jj = 0;
                int temp = (int) (Math.sqrt(re * re + im * im) / 100);
                if (temp > 255) {
                    temp = 255;
                }
                if (i < h / 2) {
                    ii = i + h / 2;
                } else {
                    ii = i - h / 2;
                }
                if (j < w / 2) {
                    jj = j + w / 2;
                } else {
                    jj = j - w / 2;
                }
                newPixels[ii * w + jj] = temp;
            }
        }

        imageAuth = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        ColorModel colorModel = imageAuth.getColorModel();
        WritableRaster raster = colorModel.createCompatibleWritableRaster(w, h);
        raster.setPixels(0, 0, w, h, newPixels);
        imageAuth.setData(raster);

        try {
//            ImageIO.write(imageAuth, "bmp", new File("fft_result.bmp"));
            ImageIO.write(imageAuth, "bmp", new File(sourceDesc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.update(g);
        return imageAuth;
    }
}
