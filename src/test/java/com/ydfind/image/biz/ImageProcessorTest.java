package com.ydfind.image.biz;

import com.ydfind.image.biz.fourier.DftProcessor;
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
//        DftProcessor.process(src, trg51, trg52, 1);
//        DftProcessor.process(src, trg501, trg502, 2);
//        DftProcessor.process(src, trg1501, trg1502, 5);
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
}
