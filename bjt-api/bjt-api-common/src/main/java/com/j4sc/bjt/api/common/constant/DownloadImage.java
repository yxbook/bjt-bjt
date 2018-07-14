package com.j4sc.bjt.api.common.constant;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @Description: 从远程服务器上下载图片到本地
 * @Author: chengyz
 * @CreateDate: 2018/5/20 16:33
 * @Version: 1.0
 **/
public class DownloadImage {

    /**
     * 下载图片并返回本地图片全路径
     * @param url 远程图片的地址
     * @param filePath 本地目录
     * @param method 请求的方法
     * @return
     */
    public static String downloadImageToLocal(String url, String filePath,String method) {
        Long beginTime = System.currentTimeMillis();
        System.out.println(beginTime);
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        String fileAbsolutePath = "";
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            String fileName = url.substring(url.lastIndexOf("/"));
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {

                filePath += "/";

            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
            String osName = System.getProperty("os.name");//获取当前系统类型
            fileName = fileName.replaceAll("/", "");
            if (Pattern.matches("Linux.*", osName)) {
                fileAbsolutePath = filePath + fileName;
            } else if (Pattern.matches("Windows.*", osName)) {
                fileAbsolutePath = filePath.replaceAll("/", "")+"\\"+fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("下载时长"+(endTime-beginTime));
        System.out.println(fileAbsolutePath);
        return fileAbsolutePath;
    }

//    public static void main(String[] args) {
//        System.out.println(downloadImageToLocal("http://p7iv3nhe5.bkt.clouddn.com/img/1524728913451_Jellyfish.jpg", "d:\\image", "GET"));
//    }
}
