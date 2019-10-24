package com.ydfind.image.biz;

import com.ydfind.image.biz.util.ExpandProcessor;
import com.ydfind.image.biz.util.HsiProcessor;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 作业7: 对一副二值图像进行膨胀、腐蚀、开、闭操作
 * @author ydfind
 * @date 2019.10.23
 */
public class Task7 {

    @Test
    public void expand() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "7/7.png";
        String trg = FileUtils.suffixToFilename(src, "-expand");
        String trg1 = FileUtils.suffixToFilename(trg, "-erosion");
        ExpandProcessor.expand(src, trg);
        ExpandProcessor.erosion(src, trg1);
    }

    @Test
    public void erosion() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "7/7.png";
        String trg = FileUtils.suffixToFilename(src, "-erosion");
        String trg1 = FileUtils.suffixToFilename(trg, "-expand");
        ExpandProcessor.erosion(src, trg);
        ExpandProcessor.expand(src, trg1);
    }

}
