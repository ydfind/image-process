package com.ydfind.image.util;

import com.ydfind.image.biz.util.ImageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像处理测试:不需要启动程序测试
 * @author ydfind
 * @date 2019.9.20
 */
@Slf4j
public class ImgUtilsTest {

    @Test
    public void testZoomByScale() throws IOException {
        String source = "D:\\book\\upload\\a.png";
        String target = "D:\\book\\upload\\ax2.png";
        ImgUtils.zoomByScale(source, target, 2.0);
    }

    @Test
    public void testAdjustGray() throws IOException {
        ImgUtils.adjustGray("D:\\book\\upload\\蝴蝶.png", "D:\\book\\upload\\蝴蝶-256.png", 256);
        ImgUtils.adjustGray("D:\\book\\upload\\蝴蝶.png", "D:\\book\\upload\\蝴蝶-16.png", 16);
        ImgUtils.adjustGray("D:\\book\\upload\\蝴蝶.png", "D:\\book\\upload\\蝴蝶-8.png", 8);
        ImgUtils.adjustGray("D:\\book\\upload\\蝴蝶.png", "D:\\book\\upload\\蝴蝶-4.png", 4);
    }

    /**
     * 灰度调整示例
     * @throws IOException 异常
     */
    @Test
    public void testGrayLineHandler() throws IOException {
        String src = "D:\\tmp\\image\\20190920122234.png";
        String trg = "D:\\tmp\\image\\20190920122234-target.png";
        final int srcStart;
        final int srcEnd;
        final int trgStart = 0;
        final int trgRange = 255 - trgStart;
        BufferedImage srcImg = ImageIO.read(new File(src));
        int[] colorExtremeValues = ImgUtils.getColorExtremeValues(srcImg);
        srcStart = colorExtremeValues[0];
        srcEnd = colorExtremeValues[1];
        log.info("处理({},{}) -> ({},{})", srcStart, srcEnd, trgStart, trgStart + trgRange);
        ImageProcessor.grayLineTransformation(src, trg, (gray) -> {
            if(gray < srcStart){
                return trgStart;
            }else if(gray > srcEnd){
                return trgStart + trgRange;
            }else{
                int newGray = new Double(
                        ((trgRange * 1.0) / (srcEnd - srcStart)) * (gray - srcStart) + trgStart).intValue();
                return newGray;
            }
        });
    }

    @Test
    public void testEqualizeHist() throws IOException {
        String src = "/imgLib/对数.png";
        String trg = "/imgLib/对数-equalizeHist-out.png";
        ImageProcessor.equalizeHist(src, trg);
    }

    @Test
    public void testLogarithmTransformation() throws IOException {
        String src = "/imgLib/logarithm.png";
        String trg = "/imgLib/logarithm-out1.png";
        ImageProcessor.logarithmTransformation(src, trg, 1);

//        String trg1 = "/imgLib/对数-out1.png";
//        ImageProcess.logarithmTransformation(trg, trg1);
    }

}
