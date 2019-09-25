package com.ydfind.image.dao;

import com.ydfind.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 图像dao
 * @author ydfind
 */
public interface ImageDao extends JpaRepository<ImageEntity, Integer> {

    /**
     * 通过图像的url进行查找
     * @param url 图像url
     * @return 图像对象
     */
    List<ImageEntity> findByUrl(String url);

}
