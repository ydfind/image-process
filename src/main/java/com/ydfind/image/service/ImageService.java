package com.ydfind.image.service;

import com.ydfind.image.common.Constant;
import com.ydfind.image.dao.ImageDao;
import com.ydfind.image.entity.ImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图像 service
 * @author ydfind
 */
@Component
@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;
    
    public ImageEntity findById(Integer imageId) {
        return imageDao.findById(imageId).get();
    }
    
    public void save(ImageEntity image) {
        imageDao.save(image);
    }

    
    public Page<ImageEntity> findAll(Pageable pageable) {
        Example<ImageEntity> example = getNotDeleteExample();
        return imageDao.findAll(example, pageable);
    }
    
    /**
     * 返回一个设置了 isDelete 为 false 的Example
     * @return
     */
    private Example<ImageEntity> getNotDeleteExample() {
        // 只查询没有删除的图书
        ImageEntity image = new ImageEntity();
        return Example.of(image);
    }

    public List<ImageEntity> findByUrl(String url){
        return imageDao.findByUrl(url);
    }

    /**
     * 将url转为下载url
     */
    public void setImageDownUrl(ImageEntity image){
        image.setDownUrl(Constant.DOWNLOAD_CONTEXT + image.getUrl());
    }
}
