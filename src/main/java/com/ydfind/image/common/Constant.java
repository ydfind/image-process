package com.ydfind.image.common;

/**
 * 常量
 * @author ydfind
 */
public class Constant {

    /**
     * 负数的符号
     */
    public static final String MINUS_SIGN = "-";

    /**
     * 灰度调整新建的图像数量
     */
    public static final Integer DEF_NEW_GREP_NUM = 4;

    /**
     * 保存上传文件的目录
     */
    public final static String FILE_DIR = isWindows() ? "D:/image/upload/" : "/image/upload/";

    public final static String DOWNLOAD_CONTEXT = "/download/";

    /**
     * 判断是否是win系统
     * @return 返回判断结果
     */
    private static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }
}
