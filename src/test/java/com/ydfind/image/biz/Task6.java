package com.ydfind.image.biz;

import com.ydfind.image.biz.util.*;
import com.ydfind.image.common.Constant;
import com.ydfind.image.util.FileUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 作业6
 * @author ydfind
 * @date 2019.10.23
 */
public class Task6 {

    @Test
    public void rgbToHsi() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "6/6.png";
        String trg = FileUtils.suffixToFilename(src, "-hsi");
        HsiProcessor.rgbToHsi(src, trg);
    }

    @Test
    public void hsiToRgb() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "6/6-hsi.png";
//        src = Constant.TEST_IMAGE_DIR + "6/6-RGB-HSI.png";
        String trg = FileUtils.suffixToFilename(src, "-rgb");
        HsiProcessor.hsiToRgb(src, trg);
    }
    @Test
    public void hsiToRed() throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "6/6-hsi.png";
//        src = Constant.TEST_IMAGE_DIR + "6/6-RGB-HSI.png";
        String trg = FileUtils.suffixToFilename(src, "-red");
        HsiProcessor.hsiToRed(src, trg);
    }

}
