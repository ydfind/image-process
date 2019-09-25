package com.ydfind.image;

import com.ydfind.image.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件上传目录设置
 * @author ydfind
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源的web访问路径，例如上传的文件 abc.png 保存在 D:/book/upload 下
     * 那么在浏览器中访问的路径为：http://localhost:8081/upload/a.png
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 判断目录是否存在，不存在就新建目录
        File path = new File(Constant.FILE_DIR);
        if (!path.exists()) {
//            path.setWritable(true, false);    //设置写权限，windows下不用此语句
            path.mkdirs();
            if (!path.exists()) {
                try {
                    throw new Exception("配置文件目录无法新建！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String dir = "file:" + Constant.FILE_DIR;
        registry.addResourceHandler(Constant.DOWNLOAD_CONTEXT + "**").addResourceLocations(dir);
    }
}
