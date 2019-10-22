package com.ydfind.image.biz;

import com.ydfind.image.biz.util.AveFilterProcessor;
import com.ydfind.image.biz.util.GaussianNoiseProcessor;
import com.ydfind.image.biz.util.MedianFilterProcessor;
import com.ydfind.image.biz.util.PepperSaltNoiseProcessor;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * 降噪音处理:不需要启动程序测试
 * @author ydfind
 * @date 2019.9.20
 */
@Slf4j
public class NoiseFilterProcessorTest {

    /**
     * 中值滤波器
     * @throws IOException 报错
     */
    @Test
    public void testMedianFilter() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "noise-old1.png";
        String src1 = Constant.TEST_IMAGE_DIR + "noise-old2.png";
        String src2 = Constant.TEST_IMAGE_DIR + "noise-old3.png";
        String trg = Constant.TEST_IMAGE_DIR + "noise-out1.png";
        String trg1 = Constant.TEST_IMAGE_DIR + "noise-out2.png";
        String trg2 = Constant.TEST_IMAGE_DIR + "noise-out3.png";
        MedianFilterProcessor.process(trg, src);
        MedianFilterProcessor.process(trg1, src1);
        MedianFilterProcessor.process(trg2, src2);
    }

    /**
     * 添加高斯噪声
     * @throws IOException 报错
     */
    @Test
    public void testGaussianNoise() throws IOException {
//        String src = Constant.TEST_IMAGE_DIR + "noise.png";
//        String trg = Constant.TEST_IMAGE_DIR + "noise-out1.png";
//        String trg1 = Constant.TEST_IMAGE_DIR + "noise-out2.png";
//        String trg2 = Constant.TEST_IMAGE_DIR + "noise-out3.png";
//        String trg3 = Constant.TEST_IMAGE_DIR + "noise-out4.png";
//        String trg4 = Constant.TEST_IMAGE_DIR + "noise-out5.png";
//        GaussianNoiseProcessor.addGaussianNoise(src, trg, 16);
//        GaussianNoiseProcessor.addGaussianNoise(src, trg1, 32);
//        GaussianNoiseProcessor.addGaussianNoise(src, trg2, 64);
//        GaussianNoiseProcessor.addGaussianNoise(src, trg3, 128);
//        GaussianNoiseProcessor.addGaussianNoise(src, trg4, 255);
        guassianNoise(16);
        guassianNoise(32);
        guassianNoise(64);
        guassianNoise(128);
        guassianNoise(255);
    }

    /**
     * 先加高斯，后平均平滑
     * @param param
     * @throws IOException
     */
    public void guassianNoise(int param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "噪声后平滑/bird.png";
        String trg = FileUtils.suffixToFilename(src, "-guassian-noise-" + param);
        String median = FileUtils.suffixToFilename(trg, "-ave-filter");
        GaussianNoiseProcessor.addGaussianNoise(src, trg, param);
        AveFilterProcessor.process(trg, median);
    }

    @Test
    public void saltNoise() throws IOException {
        saltNoise(0.8);
        saltNoise(0.85);
        saltNoise(0.9);
        saltNoise(1);
    }

    /**
     * 先加椒盐，后中值滤波器平滑
     * @param param
     * @throws IOException
     */
    public void saltNoise(double param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "4/butterfly.png";
        String trg = FileUtils.suffixToFilename(src, "-salt-noise-" + param);
        String median = FileUtils.suffixToFilename(trg, "-median-filter");
        PepperSaltNoiseProcessor.addPepperSaltNoise(src, trg, param);
        MedianFilterProcessor.process(trg, median);
    }
}
