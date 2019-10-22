package com.ydfind.image.biz;

import com.ydfind.image.biz.util.GaussianNoiseProcessor;
import com.ydfind.image.biz.util.PepperSaltNoiseProcessor;
import com.ydfind.image.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * 噪声处理测试:不需要启动程序测试
 * @author ydfind
 * @date 2019.9.20
 */
@Slf4j
public class NoiseProcessorTest {

    /**
     * 添加椒盐噪声
     * @throws IOException 报错
     */
    @Test
    public void testPepperSaltNoise() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "noise.png";
        String trg = Constant.TEST_IMAGE_DIR + "noise-out1.png";
        String trg1 = Constant.TEST_IMAGE_DIR + "noise-out2.png";
        String trg2 = Constant.TEST_IMAGE_DIR + "noise-out3.png";
        String trg3 = Constant.TEST_IMAGE_DIR + "noise-out4.png";
        PepperSaltNoiseProcessor.addPepperSaltNoise(src, trg, 0.8);
        PepperSaltNoiseProcessor.addPepperSaltNoise(src, trg1, 0.85);
        PepperSaltNoiseProcessor.addPepperSaltNoise(src, trg2, 0.9);
        PepperSaltNoiseProcessor.addPepperSaltNoise(src, trg3, 1);
    }

    /**
     * 添加高斯噪声
     * @throws IOException 报错
     */
    @Test
    public void testGaussianNoise() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "noise.png";
        String trg = Constant.TEST_IMAGE_DIR + "noise-out1.png";
        String trg1 = Constant.TEST_IMAGE_DIR + "noise-out2.png";
        String trg2 = Constant.TEST_IMAGE_DIR + "noise-out3.png";
        String trg3 = Constant.TEST_IMAGE_DIR + "noise-out4.png";
        String trg4 = Constant.TEST_IMAGE_DIR + "noise-out5.png";
        GaussianNoiseProcessor.addGaussianNoise(src, trg, 16);
        GaussianNoiseProcessor.addGaussianNoise(src, trg1, 32);
        GaussianNoiseProcessor.addGaussianNoise(src, trg2, 64);
        GaussianNoiseProcessor.addGaussianNoise(src, trg3, 128);
        GaussianNoiseProcessor.addGaussianNoise(src, trg4, 255);
    }
}
