package com.ydfind.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主程序
 * @author ydfind
 */
@SpringBootApplication
@EnableTransactionManagement
public class ImageApp {

    /**
     * 数据列表每页的数据量
     */
    public final static int PAGE_SIZE = 5;

    /**
     * 程序入口
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ImageApp.class, args);
    }

}
