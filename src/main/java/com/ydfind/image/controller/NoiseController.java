package com.ydfind.image.controller;


import com.ydfind.image.common.Constant;
import com.ydfind.image.entity.ImageEntity;
import com.ydfind.image.service.ImageService;
import com.ydfind.image.biz.ImageBaseBiz;
import com.ydfind.image.util.ImgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 噪声 controller
 * @author ydfind
 * @date 2019.9.27
 */
@Slf4j
@Controller
public class NoiseController {

    /**
     * 图像 service
     */
    @Autowired
    private ImageService imageService;



    /**
     * 打开 直方图均衡 界面
     * @param imageId 图像id
     * @param model 数据model
     */
    @GetMapping("/equalizeHistView/{imageId}")
    public String equalizeHistView(@PathVariable Integer imageId, Model model) {
        ImageEntity image = imageService.findById(imageId);
        imageService.setImageDownUrl(image);
        model.addAttribute("image", image);
        return "equalizeHistView";
    }


    /**
     * 直方图均衡
     * @param id 图像id
     * @param response 响应对象
     */
    @RequestMapping("/equalizeHist")
    public void equalizeHist(@RequestParam("id") Integer id, HttpServletResponse response) {
        try {
            ImageEntity image = imageService.findById(id);
            String filename = Constant.FILE_DIR + image.getUrl();
            // 确定文件名称
            String fileExcludeExtName = ImgUtils.getImgNameExcludeExt(filename);
            String changeName = fileExcludeExtName + "_equalizeHist";
            String trgFilename = filename.replace(fileExcludeExtName, changeName);
            // 进行直方图均衡
            log.info("直方图均衡 结果文件：{}", trgFilename);
            ImageBaseBiz.equalizeHist(filename, trgFilename);
            // 结果保存数据库
            ImageEntity equalizeImg;
            String trgUrl = image.getUrl().replace(fileExcludeExtName, changeName);
            List<ImageEntity> images = imageService.findByUrl(trgUrl);
            if (Objects.isNull(images) || images.size() < 1) {
                equalizeImg = new ImageEntity();
                equalizeImg.setImgName(image.getImgName());
                equalizeImg.setUrl(trgUrl);
                equalizeImg.setCreatedAt(new Date());
                equalizeImg.setCreatedBy("SYSTEM");
                equalizeImg.setUpdatedAt(equalizeImg.getCreatedAt());
                equalizeImg.setUpdatedBy(equalizeImg.getCreatedBy());
                imageService.save(equalizeImg);
            }else{
                equalizeImg = images.get(0);
            }
            imageService.setImageDownUrl(equalizeImg);
            // 结果响应
            String url = equalizeImg.getDownUrl();
            log.info("直方图均衡 = " + url);
            response.getWriter().write(url);
        }catch (Exception e){
            log.error("图像 直方图均衡 报错：", e);
        }
    }

}
