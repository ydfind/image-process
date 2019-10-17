package com.ydfind.image.biz;

import com.ydfind.image.biz.util.DftForwardProcessor;
import com.ydfind.image.biz.util.DftProcessor;
import com.ydfind.image.biz.util.DftReverseProcessor;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.common.Constant;
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

    @Test
    public void testDftProcessor() throws IOException {
        String src;
        src = Constant.TEST_IMAGE_DIR + "old64x64.png";
        String trg51 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-5.png";
        String trg52 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-5-old.png";
        String trg501 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-50.png";
        String trg502 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-50-old.png";
        String trg1501 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-150.png";
        String trg1502 = Constant.TEST_IMAGE_DIR + "old64x64-SCOPE-150-old.png";
//        DftProcessor.process(src, trg51, trg52, 1);
//        DftProcessor.process(src, trg501, trg502, 2);
//        DftProcessor.process(src, trg1501, trg1502, 5);
        DftProcessor.process(src, trg51, trg52, 5);
        DftProcessor.process(src, trg501, trg502, 50);
        DftProcessor.process(src, trg1501, trg1502, 150);
    }
}
