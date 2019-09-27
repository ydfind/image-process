package com.ydfind.image.service;

import com.ydfind.image.ImageApp;
import com.ydfind.image.entity.ImageEntity;
import com.ydfind.image.biz.ImageBaseBiz;
import com.ydfind.image.util.ImgUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImageApp.class)
public class ImageServiceTest {

    @Autowired
    public ImageService imageService;

    @Test
    public void testSaveImage(){
        ImageEntity image = new ImageEntity();
        image.setImgName("测试图片");
        image.setUrl("D://usafaw/dafew");
        image.setCreatedAt(new Date());
        image.setCreatedBy("SYSTEM");
        image.setUpdatedAt(image.getCreatedAt());
        image.setUpdatedBy(image.getCreatedBy());
        imageService.save(image);
    }

    @Test
    public void testDir() throws IOException {
        Resource resource = new ClassPathResource("/image/equalizeHist.png");
        File file = resource.getFile();
        System.out.println("name = " + file.getName());
        System.out.println("path = " + file.getPath());
    }

    /**
     * 灰度调整示例
     * @throws IOException 异常
     */
    @Test
    public void testGrayLineHandler() throws IOException {
        String src = "/image/gray_line_handle.png";
        String trg;
        Resource resource = new ClassPathResource(src);
        File file = resource.getFile();
        src = file.getPath();
        trg = src.replace("gray_line_handle.png", "gray_line_handle_out.png");

        final int srcStart;
        final int srcEnd;
        final int trgStart = 50;
        final int trgRange = 255 - trgStart;
        BufferedImage srcImg = ImageIO.read(new File(src));
        int[] colorExtremeValues = ImgUtils.getColorExtremeValues(srcImg);
        srcStart = colorExtremeValues[0];
        srcEnd = colorExtremeValues[1];
        log.info("处理({},{}) -> ({},{})", srcStart, srcEnd, trgStart, trgStart + trgRange);
        ImageBaseBiz.grayLineTransformation(src, trg, (gray) -> {
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
        String src = "/image/equalizeHist.png";
        String trg;
        Resource resource = new ClassPathResource(src);
        File file = resource.getFile();
        src = file.getPath();
        trg = src.replace("equalizeHist.png", "equalizeHist_out.png");

        ImageBaseBiz.equalizeHist(src, trg);
    }
}
