package com.ydfind.image.biz;

import com.ydfind.image.biz.fourier.*;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 图像处理 测试类
 * @author ydfind
 * @date 2019.10.17
 */
public class ImageProcessorTest {

    /**
     * 图像黑白化
     */
    @Test
    public void testProcessBlackAndWhite() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "LENA.png";
        String trg = Constant.TEST_IMAGE_DIR + "LENA-black-white.png";
        ImageProcessor.processBlackAndWhite(src, trg);
    }

    /**
     * 傅里叶变换
     * 输出
     * time = 8775
     * time = 3538
     * time = 3441
     * @throws IOException
     */
    @Test
    public void testDftProcessor() throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/LENA128x128.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old128x128.png";
        String trg51 = FileUtils.suffixToFilename(src, "-SCOPE-5");
        String trg52 = FileUtils.suffixToFilename(src, "-DFT-5");
        String trg501 = FileUtils.suffixToFilename(src, "-SCOPE-50");
        String trg502 = FileUtils.suffixToFilename(src, "-DFT-50");
        String trg1501 = FileUtils.suffixToFilename(src, "-SCOPE-150");
        String trg1502 = FileUtils.suffixToFilename(src, "-DFT-150");
        long time = System.currentTimeMillis();
        DftProcessor.process(src, trg51, trg52, 5);
        System.out.println("time = " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        DftProcessor.process(src, trg501, trg502, 50);
        System.out.println("time = " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        DftProcessor.process(src, trg1501, trg1502, 150);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    /*****************************标准低通、高通、锐化滤波*****************************************************/
    /**
     * 标准低通滤波
     * @throws IOException 异常
     */
    @Test
    public void testIlpfProcessor() throws IOException {
        processIlpProcessor(5);
        processIlpProcessor(10);
//        processIlpProcessor(20);
//        processIlpProcessor(30);
    }

    public void processIlpProcessor(int radius) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old64x64.png";
//        src = Constant.TEST_IMAGE_DIR + "傅里叶/old128x128.png";
        String trg1 = FileUtils.suffixToFilename(src, "-FOURIER-" + radius);
        String fourier1 = FileUtils.suffixToFilename(src, "-ILPF-" + radius);
        long time = System.currentTimeMillis();
        IlpfFilterProcessor.process(src, trg1, fourier1, radius);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    @Test
    public void testIhpfProcessor() throws IOException {
        processIhpProcessor(5);
//        processIhpProcessor(10);
//        processIhpProcessor(20);
//        processIhpProcessor(30);
    }

    public void processIhpProcessor(int radius) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/hltp64x64.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/hltp128x128.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-IHPF-FOURIER-" + radius);
        String trg1 = FileUtils.suffixToFilename(src, "-IHPF-" + radius);
        long time = System.currentTimeMillis();
        IhpfFilterProcessor.process(src, fourier1, trg1, radius);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }
    @Test
    public void testIhpfProcessor1() throws IOException {
        processIhpProcessor1(5, 2);
        processIhpProcessor1(5, 2.7);
//        processIhpProcessor(10);
//        processIhpProcessor(20);
//        processIhpProcessor(30);
    }

    public void processIhpProcessor1(int radius, double a) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/hltp64x64.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/hltp128x128.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-IHPF-FOURIER-" + radius + "-" + a);
        String trg1 = FileUtils.suffixToFilename(src, "-IHPF-" + radius + "-" + a);
        long time = System.currentTimeMillis();
        IhpfFilterProcessor.process(src, fourier1, trg1, radius, a);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    /*****************************巴特沃思低通、高通、锐化滤波器(BLPF)*****************************************************/
    /**
     * 巴特沃思低通滤波器(BLPF)
     * @throws IOException 异常
     */
    @Test
    public void testBlpfProcessor() throws IOException {
        processBlpProcessor(5, 2);
        processBlpProcessor(10, 2);
        processBlpProcessor(20, 2);
        processBlpProcessor(30, 2);
        processBlpProcessor(50, 2);
        processBlpProcessor(60, 2);
    }

    public void processBlpProcessor(int radius, int n) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old64x64.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old128x128.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-BLPF-FOURIER-" + radius);
        String trg1 = FileUtils.suffixToFilename(src, "-BLPF-" + radius);
        long time = System.currentTimeMillis();
        BlpfFilterProcessor.process(src, trg1, fourier1, radius, n);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    @Test
    public void testBhpfProcessor() throws IOException {
        processBhpProcessor(5, 2);
        processBhpProcessor(10, 2);
        processBhpProcessor(20, 2);
        processBhpProcessor(30, 2);
        processBhpProcessor(50, 2);
        processBhpProcessor(60, 2);
    }

    public void processBhpProcessor(int radius, int n) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/bltp64x32.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/bltp128x64.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-HLPF-FOURIER-" + radius);
        String trg1 = FileUtils.suffixToFilename(src, "-HLPF-" + radius);
        long time = System.currentTimeMillis();
        BhpfFilterProcessor.process(src, trg1, fourier1, radius, n);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    @Test
    public void testBhpfProcessor1() throws IOException {
        processBhpProcessor1(5, 2, 0.5, 2);
//        processBhpProcessor1(10, 2, 0.5, 2);
//        processBhpProcessor1(20, 2, 0.5, 2);
//        processBhpProcessor1(30, 2, 0.5, 2);
//        processBhpProcessor1(50, 2, 0.5, 2);
//        processBhpProcessor1(60, 2, 0.5, 2);
    }

    public void processBhpProcessor1(int radius, int n, double a, double b) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/bltp64x32.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/bltp128x64.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-HLPF-FOURIER-" + radius + "-" + a + "-" + b);
        String trg1 = FileUtils.suffixToFilename(src, "-HLPF-" + radius + "-" + a + "-" + b);
        long time = System.currentTimeMillis();
        BhpfFilterProcessor.process(src, trg1, fourier1, radius, n, a, b);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    /*****************************GLPF-高斯低通滤波器*****************************************************/
    /**
     * GLPF-高斯低通滤波器
     * @throws IOException 异常
     */
    @Test
    public void testGlpfProcessor() throws IOException {
        processGlpProcessor(5);
        processGlpProcessor(10);
        processGlpProcessor(20);
        processGlpProcessor(30);
        processGlpProcessor(50);
        processGlpProcessor(60);
    }

    public void processGlpProcessor(int radius) throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old64x64.png";
        src = Constant.TEST_IMAGE_DIR + "傅里叶/old128x128.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-GLPF-FOURIER-" + radius);
        String trg1 = FileUtils.suffixToFilename(src, "-GLPF-" + radius);
        long time = System.currentTimeMillis();
        GlpfFilterProcessor.process(src, trg1, fourier1, radius);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

    /***********************************拉普拉斯-增强*****************************************************************/
    @Test
    public void testSharpenLaspaceProcessor() throws IOException {
        processSharpenLaspaceProcessor();
    }

    public void processSharpenLaspaceProcessor() throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "傅里叶/dim64x64.png";
//        src = Constant.TEST_IMAGE_DIR + "傅里叶/old128x128.png";
        String fourier1 = FileUtils.suffixToFilename(src, "-LASPACE-FOURIER-");
        String trg1 = FileUtils.suffixToFilename(src, "-LASPACE-");
        long time = System.currentTimeMillis();
        SharpenLaspaceProcessor.process(src, trg1, fourier1);
        System.out.println("time = " + (System.currentTimeMillis() - time));
    }

}
