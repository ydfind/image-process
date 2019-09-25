package com.ydfind.image.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.FilterJoinTable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 图像实体
 * @author ydfind
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "IMAGE")
public class ImageEntity extends BaseEntity {

    @Column(name = "IMG_NAME")
    private String imgName;
    
    @Column(name = "URL")
    private String url;

    @Transient
    private String downUrl;
}
