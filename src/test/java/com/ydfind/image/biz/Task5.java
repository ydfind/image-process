package com.ydfind.image.biz;

import com.ydfind.image.biz.util.*;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import org.junit.Test;

import java.awt.geom.GeneralPath;
import java.io.IOException;

/**
 * 作业5
 * @author ydfind
 * @date 2019.10.20
 */
public class Task5 {

    /**
     * 高斯后，算术均值
     */
    @Test
    public void testGuassionAndMean() throws IOException {
        guassionAndMean(32);
    }

    public void guassionAndMean(int param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512.png";
        String trg = FileUtils.suffixToFilename(src, "-guassian-" + param);
        String median = FileUtils.suffixToFilename(trg, "-mean-filter");
        GaussianNoiseProcessor.addGaussianNoise(src, trg, param);
//        MeanFilterProcessor.process(trg, median);
    }

    /**
     * 高斯后，几何均值
     * @throws IOException
     */
    @Test
    public void testGuassionAndGeometry() throws IOException {
        guassionAndGeometry(64);
    }


    public void guassionAndGeometry(int param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512.png";
        String trg = FileUtils.suffixToFilename(src, "-guassian-" + param);
        String median = FileUtils.suffixToFilename(trg, "-geometry-filter");
        GaussianNoiseProcessor.addGaussianNoise(src, trg, param);
        GeometryFilterProcessor.process(trg, median);
    }

    /**
     * 先胡椒噪声，再逆谐波滤波
     */
    @Test
    public void testNoiseAndReHarmonicFilter() throws IOException {
        pepperAndReHarmonicFilter(0.9, 1.5, -1.5);
        saltAndHarmonicFilter(0.95, 1.5, -1.5);
    }

    /**
     * 先胡椒噪声，再逆谐波滤波(正负参数）
     */
    public void pepperAndReHarmonicFilter(double param, double param1, double param2) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512.png";
        String trg = FileUtils.suffixToFilename(src, "-pepper-" + param);
        String filter1 = FileUtils.suffixToFilename(trg, "-reharmonic-" + param1);
        String filter2 = FileUtils.suffixToFilename(trg, "-reharmonic-" + param2);
        PepperSaltNoiseProcessor.addNoise(src, trg, param, PepperSaltNoiseProcessor.PEPPER_NOISE_NAME);
        ReHarmonicFilterProcessor.process(trg, filter1, param1);
        ReHarmonicFilterProcessor.process(trg, filter2, param2);
    }

    /**
     * 先盐噪声，再逆谐波滤波(正负参数）
     */
    public void saltAndHarmonicFilter(double param, double param1, double param2) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512.png";
        String trg = FileUtils.suffixToFilename(src, "-salt-" + param);
        String filter1 = FileUtils.suffixToFilename(trg, "-harmonic-" + param1);
        String filter2 = FileUtils.suffixToFilename(trg, "-harmonic-" + param2);
        PepperSaltNoiseProcessor.addNoise(src, trg, param, PepperSaltNoiseProcessor.SALT_NOISE_NAME);
        ReHarmonicFilterProcessor.process(trg, filter1, param1);
        ReHarmonicFilterProcessor.process(trg, filter2, param2);
    }
    /***************************************************单个测试************************************************/
    @Test
    public void testReHarmonicFilter() throws IOException {
        reHarmonicFilter(-1.5);
//        reHarmonicFilter(1.2);
//        reHarmonicFilter(3.5);
//        reHarmonicFilter(4.5);
//        reHarmonicFilter(5.5);
//        reHarmonicFilter(6.5);
//        reHarmonicFilter(7.5);
//        reHarmonicFilter(8.5);

    }

    public void reHarmonicFilter(double param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/pepper512x512.png";
        src = Constant.TEST_IMAGE_DIR + "5/5-1-512x512-salt-0.9.png";
        String trg = FileUtils.suffixToFilename(src, "-reharmonic-" + param);
        ReHarmonicFilterProcessor.process(src, trg, param);
    }


}
