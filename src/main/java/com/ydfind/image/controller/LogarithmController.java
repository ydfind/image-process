package com.ydfind.image.controller;

import com.ydfind.image.common.Constant;
import com.ydfind.image.entity.ImageEntity;
import com.ydfind.image.service.ImageService;
import com.ydfind.image.util.ImageProcess;
import com.ydfind.image.util.ImgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 图像controller
 * @author ydfind
 * @date 2019.9.21
 */
@Slf4j
@Controller
public class LogarithmController {


    /**
     * 图像 service
     */
    @Autowired
    private ImageService imageService;

    /**
     * 打开 灰度拉伸 界面
     * @param imageId 图像id
     * @param model 数据model
     */
    @GetMapping("/imgView/{imageId}")
    public String imgView(@PathVariable Integer imageId, Model model) {
        ImageEntity image = imageService.findById(imageId);
        imageService.setImageDownUrl(image);
        model.addAttribute("image", image);
        return "imgView";
    }

    /**
     * 对数变换
     * @param id 图像id
     * @param response 响应对象
     */
    @RequestMapping("/logarithm")
    public void logarithm(@RequestParam("id") Integer id, @RequestParam("param1") String  param1,
                          HttpServletResponse response) {
        try {
            ImageEntity image = imageService.findById(id);
            String filename = Constant.FILE_DIR + image.getUrl();
            // 确定文件名称
            String fileExcludeExtName = ImgUtils.getImgNameExcludeExt(filename);
            String changeName = fileExcludeExtName + "_logarithm";
            String trgFilename = filename.replace(fileExcludeExtName, changeName);
            // 处理
            log.info("对数变换 结果文件：{}", trgFilename);
            double mult = Double.valueOf(param1);
            ImageProcess.logarithmTransformation(filename, trgFilename, mult);
            // 结果保存数据库
            ImageEntity entity;
            String trgUrl = image.getUrl().replace(fileExcludeExtName, changeName);
            List<ImageEntity> images = imageService.findByUrl(trgUrl);
            if (Objects.isNull(images) || images.size() < 1) {
                entity = new ImageEntity();
                entity.setImgName(image.getImgName());
                entity.setUrl(trgUrl);
                entity.setCreatedAt(new Date());
                entity.setCreatedBy("SYSTEM");
                entity.setUpdatedAt(entity.getCreatedAt());
                entity.setUpdatedBy(entity.getCreatedBy());
                imageService.save(entity);
            }else{
                entity = images.get(0);
            }
            imageService.setImageDownUrl(entity);
            // 结果响应
            String url = entity.getDownUrl();
            log.info("对数变换  = " + url);
            response.getWriter().write(url);
        }catch (Exception e){
            log.error("图像 对数变换 报错：", e);
        }
    }



}
