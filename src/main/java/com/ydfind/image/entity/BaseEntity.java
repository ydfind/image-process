package com.ydfind.image.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 该类是所有实体类的父类，它不会映射表，但其属性会映射子类的相应字段
 * @author ydfind
 */
@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
