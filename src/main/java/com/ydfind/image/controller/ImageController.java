package com.ydfind.image.controller;

import com.ydfind.image.ImageApp;
import com.ydfind.image.common.Constant;
import com.ydfind.image.entity.ImageEntity;
import com.ydfind.image.service.ImageService;
import com.ydfind.image.util.FileUtils;
import com.ydfind.image.biz.util.ImageProcessor;
import com.ydfind.image.util.ImgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * 图像controller
 * @author ydfind
 * @date 2019.9.21
 */
@Slf4j
@Controller
public class ImageController {

    /**
     * 图像 service
     */
    @Autowired
    private ImageService imageService;

    /**
     * 首页
     * @param model 数据model
     * @param pageable 分页参数
     * @return 显示页面
     */
    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = ImageApp.PAGE_SIZE) Pageable pageable) {
        Page<ImageEntity> images = imageService.findAll(pageable);
        for(ImageEntity image: images){
            imageService.setImageDownUrl(image);
        }
        model.addAttribute("datas", images);
        return "index";
    }

    /**
     * 上传，并返回图像链接
     * @param uploadFile 上传的文件
     * @param response 响应参数
     */
    @RequestMapping("/upload")
    public void upload(@RequestParam("filename") MultipartFile uploadFile, HttpServletResponse response) {
        try {
            byte[] content = uploadFile.getBytes();
            // 保存文件到具体目录，此处为D:/book/upload
            String path = Constant.FILE_DIR;
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // 获取文件后缀
            String fileSuffix = FileUtils
                    .getSuffix(uploadFile.getOriginalFilename());
            // 设置文件名
            String filename = folder.getAbsolutePath() + File.separator
                    + UUID.randomUUID().toString() + fileSuffix;
            System.out.println("新建文件为 = " + filename);
            File file = new File(filename);
            file.createNewFile();

            // 保存到数据库
            ImageEntity image;
            image = new ImageEntity();
            image.setImgName(uploadFile.getOriginalFilename());
            image.setUrl(file.getName());
            image.setCreatedAt(new Date());
            image.setCreatedBy("SYSTEM");
            image.setUpdatedAt(image.getCreatedAt());
            image.setUpdatedBy(image.getCreatedBy());
            imageService.save(image);

            // 写到服务器文件
            FileUtils.writeFile(file, content);
            response.getWriter().write(Constant.DOWNLOAD_CONTEXT + file.getName());
        }catch (Exception e){
            log.error("图像上传报错：", e);
        }
    }

    /**************************************缩放及降低灰度*****************************************************/
    /**
     * 打开操作界面
     * @param imageId 图像id
     * @param model 数据model
     */
    @GetMapping("/operate/{imageId}")
    public String operateView(@PathVariable Integer imageId, Model model) {
        ImageEntity image = imageService.findById(imageId);
        imageService.setImageDownUrl(image);
        model.addAttribute("image", image);
        return "operate";
    }
    /**
     * 放大缩小
     * @param zoom 缩放因子
     * @param id 编辑的图像id
     * @param response 响应参数
     */
    @RequestMapping("/scale")
    public void scale(@RequestParam("zoom") String zoom, @RequestParam("id") Integer id,
                      HttpServletResponse response) {
        try {
            ImageEntity image = imageService.findById(id);
            String filename = Constant.FILE_DIR + image.getUrl();
            zoom = zoom.replace("%", "");
            double scaleLevel;
            if (zoom.contains(Constant.MINUS_SIGN)) {
                zoom = zoom.replace("-", "");
                scaleLevel = Double.valueOf(zoom) / 100.0;
                if (scaleLevel > 1) {
                    scaleLevel = 1;
                }
                scaleLevel = 1 - scaleLevel;
            } else {
                scaleLevel = Double.valueOf(zoom) / 100.0;
                if (scaleLevel > 1) {
                    scaleLevel = 1;
                }
                scaleLevel += 1;
            }
            ImgUtils.zoomByScale(filename, filename, scaleLevel);
            imageService.setImageDownUrl(image);
            response.getWriter().write(image.getDownUrl());
        }catch (Exception e){
            log.error("图像缩放报错：", e);
        }
    }

    /**
     * 灰度调整
     * @param id 图像id
     * @param response 响应对象
     */
    @RequestMapping("/adjustGray")
    public void adjustGray(@RequestParam("id") Integer id, HttpServletResponse response) {
        try {
            ImageEntity image = imageService.findById(id);
            String filename = Constant.FILE_DIR + image.getUrl();
            List<ImageEntity> newImages = new ArrayList<>();
            List<String> filenames = new ArrayList<>();


            String oldUrl = image.getUrl();
            //得到最后一个.的位置
            int index = oldUrl.lastIndexOf(".");
            //获取被缩放的图片的格式
            String ext = oldUrl.substring(index + 1);
            //获取目标路径(和原始图片路径相同,在文件名后添加了一个_s)
            String destFile = oldUrl.substring(0, index) + "_grey1." + ext;
            List<ImageEntity> images = imageService.findByUrl(destFile);
            if (Objects.isNull(images) || images.size() < 1) {
                for (int i = 1; i <= Constant.DEF_NEW_GREP_NUM; i++) {
                    ImageEntity greyImg = new ImageEntity();
                    greyImg.setImgName(image.getImgName());
                    greyImg.setUrl(oldUrl.substring(0, index) + "_grey" + i + "." + ext);
                    greyImg.setCreatedAt(new Date());
                    greyImg.setCreatedBy("SYSTEM");
                    greyImg.setUpdatedAt(greyImg.getCreatedAt());
                    greyImg.setUpdatedBy(greyImg.getCreatedBy());
                    imageService.save(greyImg);
                    imageService.setImageDownUrl(greyImg);
                    newImages.add(greyImg);

                    String greyFilename = Constant.FILE_DIR + greyImg.getUrl();
                    filenames.add(greyFilename);

                    Files.copy(new File(filename).toPath(), new File(greyFilename).toPath());
                }
            } else {
                imageService.setImageDownUrl(images.get(0));
                String greyFilename = Constant.FILE_DIR + images.get(0).getUrl();
                filenames.add(greyFilename);
                newImages.add(images.get(0));
                for (int i = 2; i <= Constant.DEF_NEW_GREP_NUM; i++) {
                    destFile = oldUrl.substring(0, index) + "_grey" + (newImages.size() + 1) + "." + ext;
                    images = imageService.findByUrl(destFile);
                    imageService.setImageDownUrl(images.get(0));
                    newImages.add(images.get(0));

                    greyFilename = Constant.FILE_DIR + images.get(0).getUrl();
                    filenames.add(greyFilename);
                }
            }


            ImgUtils.adjustGray(filename, filenames.get(0), 256);
            ImgUtils.adjustGray(filename, filenames.get(1), 16);
            ImgUtils.adjustGray(filename, filenames.get(2), 8);
            ImgUtils.adjustGray(filename, filenames.get(3), 4);

            String url = newImages.get(0).getDownUrl() + "," + newImages.get(1).getDownUrl() + "," +
                    newImages.get(2).getDownUrl() + "," + newImages.get(3).getDownUrl();
            log.info("灰度图片urls = " + url);
            response.getWriter().write(url);
        }catch (Exception e){
            log.error("图像灰度调整报错：", e);
        }
    }


    /************************************** 灰度拉伸 及 直方图均衡 *********************************************/
    /**
     * 打开 灰度拉伸 界面
     * @param imageId 图像id
     * @param model 数据model
     */
    @GetMapping("/grayLineView/{imageId}")
    public String grayLineView(@PathVariable Integer imageId, Model model) {
        ImageEntity image = imageService.findById(imageId);
        imageService.setImageDownUrl(image);
        model.addAttribute("image", image);
        return "gray/lineView";
    }

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
     * 灰度拉伸
     * @param id 图像id
     * @param response 响应对象
     */
    @RequestMapping("/grayLine")
    public void grayLine(@RequestParam("id") Integer id, HttpServletResponse response) {
        try {
            ImageEntity image = imageService.findById(id);
            String filename = Constant.FILE_DIR + image.getUrl();
            // 计算拉伸范围
            BufferedImage srcImg = ImageIO.read(new File(filename));
            int[] colorExtremeValues = ImgUtils.getColorExtremeValues(srcImg);
            int srcStart = colorExtremeValues[0];
            int srcEnd = colorExtremeValues[1];
            final int trgStart = 0;
            final int trgRange = 255 - trgStart;
            // 根据拉伸范围确定文件名称
            String fileExcludeExtName = ImgUtils.getImgNameExcludeExt(filename);
            String changeName = fileExcludeExtName + "_grayLine_" + srcStart + "_" +
                    srcEnd + "_" + trgStart + "_" + (trgStart + trgRange);
            String trgFilename = filename.replace(fileExcludeExtName, changeName);
            // 进行灰度拉伸
            log.info("灰度拉伸 结果文件：{}", trgFilename);
            BufferedImage trgImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            ImageProcessor.grayLineTransformation(srcImg, trgImg, (gray) -> {
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
            // 结果保存文件、数据库
            ImgUtils.saveImage(trgFilename, trgImg);

            ImageEntity greyImg;
            String trgUrl = image.getUrl().replace(fileExcludeExtName, changeName);
            List<ImageEntity> images = imageService.findByUrl(trgUrl);
            if (Objects.isNull(images) || images.size() < 1) {
                greyImg = new ImageEntity();
                greyImg.setImgName(image.getImgName());
                greyImg.setUrl(trgUrl);
                greyImg.setCreatedAt(new Date());
                greyImg.setCreatedBy("SYSTEM");
                greyImg.setUpdatedAt(greyImg.getCreatedAt());
                greyImg.setUpdatedBy(greyImg.getCreatedBy());
                imageService.save(greyImg);
            }else{
                greyImg = images.get(0);
            }
            imageService.setImageDownUrl(greyImg);
            // 结果响应
            String url = greyImg.getDownUrl();
            log.info("灰度图片urls = " + url);
            response.getWriter().write(url);
        }catch (Exception e){
            log.error("图像 灰度拉伸 报错：", e);
        }
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
            ImageProcessor.equalizeHist(filename, trgFilename);
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
