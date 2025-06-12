package com.sp.yangshengai.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl {

    @Value("${file.path}")
    private String filePath;

    @Value("${file.PublicNetworkIp}")
    private String publicNetworkIp;
    /**
     * 文件上传方法
     * 该方法接收一个multipart文件作为输入，将其上传到服务器，并返回文件的访问地址
     *
     * @param file 要上传的multipart文件
     * @return 文件的访问地址，如果上传失败则返回null
     */
    public String upload(MultipartFile file) {
        try {
            // 获取文件原始名称
            String filename = file.getOriginalFilename();
            // 提取文件扩展名
            String ext = filename.substring(filename.lastIndexOf("."));
            // 生成新的文件名，使用UUID确保唯一性，避免文件名冲突
            String newfile = UUID.randomUUID().toString().replace("-", "") + ext;
            // 创建文件路径对象
            File pathname = new File(filePath + newfile);
            // 将文件转移到指定路径
            file.transferTo(pathname);

            // 返回文件的网络访问地址
            return publicNetworkIp+newfile;
        } catch (IOException e) {
            // 打印异常信息
            e.printStackTrace();
            // 如果发生IO异常，返回null
            return null;
        }

    }
}
