package com.ydfind.image.biz;

import com.ydfind.image.biz.util.AveFilterProcessor;
import com.ydfind.image.biz.util.GaussianNoiseProcessor;
import com.ydfind.image.biz.util.GeometryFilterProcessor;
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
        String src = Constant.TEST_IMAGE_DIR + "5/dian.png";
        String trg = FileUtils.suffixToFilename(src, "-guassian-" + param);
        String median = FileUtils.suffixToFilename(trg, "-mean-filter");
        GaussianNoiseProcessor.addGaussianNoise(src, trg, param);
        AveFilterProcessor.process(trg, median);
    }

    /**
     * 高斯后，几何均值
     * @throws IOException
     */
    public void testGuassionAndGeometry() throws IOException {
        guassionAndGeometry(32);
    }


    public void guassionAndGeometry(int param) throws IOException {
        String src = Constant.TEST_IMAGE_DIR + "5/dian.png";
        String trg = FileUtils.suffixToFilename(src, "-guassian-" + param);
        String median = FileUtils.suffixToFilename(trg, "-geometry-filter");
        GaussianNoiseProcessor.addGaussianNoise(src, trg, param);
        GeometryFilterProcessor.process(trg, median);
    }

    /**
     * 先
     */
    public void saltAndHarmonicFilter(){

    }


}
