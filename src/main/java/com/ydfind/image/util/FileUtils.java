package com.ydfind.image.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件工具类
 * @author ydfind
 * @date 2019.9.20
 */
@Slf4j
public class FileUtils {

    /**
     * 将string写入文件
     * @param path 带路径文件名
     * @param str 内容
     * @throws IOException 写入异常
     */
    public static void writeFile(String path, String str) throws IOException {
        try {
            System.out.println(path);
            File file = new File(path);
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StringBuffer sb = new StringBuffer();
            sb.append(str + "\r\n");
            out.write(sb.toString().getBytes("utf-8"));
            out.close();
        } catch(IOException ex) {
            throw new IOException("writeFile " + path + " error", ex);
        }
    }

    public static String readFile(String path) throws IOException {
        return readFile(path, "utf-8");
    }

    public static String readFile(String path, String encode) throws IOException {
        StringBuffer sb=new StringBuffer();
        String tempStr;
        try {
            File file=new File(path);
            if(!file.exists()) {
                throw new FileNotFoundException();
            }
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis, encode));
            while((tempStr=br.readLine())!=null) {
                sb.append(tempStr + "\r\n");
            }
            if(sb.length() > 0){
                sb.delete(sb.length() - 2, sb.length());
            }
        } catch(IOException ex) {
            throw new IOException("readFile " + path + " error", ex);
        }
        return sb.toString();
    }

    public static List<String> readFileList(String path, String encode) throws IOException {
//        StringBuffer sb=new StringBuffer();
        ArrayList<String> list = new ArrayList<>();
        String tempstr=null;
        try {
            File file=new File(path);
            if(!file.exists()) {
                throw new FileNotFoundException();
            }
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis, encode));
            while((tempstr=br.readLine())!=null) {
                list.add(tempstr);
            }
        } catch(IOException ex) {
            throw new IOException("readFile " + path + " error", ex);
        }
        return list;
    }

    public static String findByFilename(String path, String filename){
        String name = getFilenameWithoutPath(filename);
        File pathFile = new File(path);
        return findByFilename(pathFile, name);
    }

    public static String findFirstByFilename(String path, String filename){
        String name = getFilenameWithoutPath(filename);
        File pathFile = new File(path);
        return findFirstByFilename(pathFile, name);
    }

    private static String findByFilename(File path, String filename){
        File[] files = path.listFiles();
        for(File file: files){
            if(file.isFile()){
                if(filename.equals(getFilenameWithoutPath(file.getPath()))){
                    return file.getPath();
                }
            }else if(file.isDirectory()){
                String result = findByFilename(file, filename);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    private static String findFirstByFilename(File path, String filename){
        File[] files = path.listFiles();
        for(File file: files){
            if(file.isFile()){
                if(filename.equals(getFilenameWithoutPath(file.getPath()))){
                    return file.getPath();
                }
            }
        }
        for(File file: files){
            if(file.isDirectory()){
                String result = findByFilename(file, filename);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    public static String getFilenameWithoutPath(String filename){
        String name = filename;
        int lastId = filename.lastIndexOf("/");
        if(lastId < 0){
            lastId = filename.lastIndexOf("\\");
        }
        if(lastId >= 0){
            name = filename.substring(lastId + 1);
        }
        return name;
    }

    public static void writeFile(String path, List<String> list) throws IOException {
        StringBuffer sb = new StringBuffer();
        for(String str: list){
            sb.append(str + "\r\n");
        }
        if(sb.length() > 0){
            sb.delete(sb.length() - 2, sb.length());
        }
        writeFile(path, sb.toString());
    }

    public static void writeFile(String path, String[] list) throws IOException {
        StringBuffer sb = new StringBuffer();
        for(String str: list){
            sb.append(str + "\r\n");
        }
        if(sb.length() > 0){
            sb.delete(sb.length() - 2, sb.length());
        }
        writeFile(path, sb.toString());
    }

    public static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            if(!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            if(!dest.exists()){
                dest.createNewFile();
            }

            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
    /**
     * 大文件拷贝
     */

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            if(!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            if(!dest.exists()){
                dest.createNewFile();
            }

            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }


    public static void createDirIfNotExist(String path){
        File file = new File(path);
        createDirIfNotExist(file);
    }

    public static void createDirIfNotExist(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

//    public static void createParentDirIfNotExist(String filename){
//        File file = new File(filename);
//        createParentDirIfNotExist(file);
//    }

    public static void createParentDirIfNotExist(File file){
        createDirIfNotExist(file.getParentFile());
    }

    public static Boolean isDirEnd(String name){
        if(Objects.nonNull(name)){
            if(name.endsWith("/") || name.endsWith("\\")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件连接符
     */
    private static final String FILE_JOIN_MARK = ".";

    /**
     * 将 content 写到file中
     * @param file
     * @param content
     */
    public static void writeFile(File file, byte[] content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件名的后缀
     * @param filename 文件名
     * @return 带.文件类型
     */
    public static String getSuffix(String filename) {
        if(filename.indexOf(FILE_JOIN_MARK) == -1) {
            return "";
        }
        int dotIndex = filename.lastIndexOf(FILE_JOIN_MARK);
        String result = filename.substring(dotIndex);
        return result;
    }

    /**
     * 文件名称 添加后缀
     * @param filename 原文件名称 old.png
     * @param suffix 后缀 -suffix
     * @return 新文件名称old-suffix.png
     */
    public static String suffixToFilename(String filename, String suffix){
        if(filename.indexOf(FILE_JOIN_MARK) == -1) {
            return "";
        }
        int dotIndex = filename.lastIndexOf(FILE_JOIN_MARK);
        String result = filename.substring(0, dotIndex) + suffix + filename.substring(dotIndex, filename.length());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(FileUtils.getSuffix("abc"));
        System.out.println(FileUtils.getSuffix("abc.b.c.png"));

        System.out.println(FileUtils.suffixToFilename("abc.b.c.png", "-suffix"));
    }
}
