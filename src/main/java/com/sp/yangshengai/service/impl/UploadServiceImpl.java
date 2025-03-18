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
    public String upload(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf("."));
            String newfile = UUID.randomUUID().toString().replace("-", "") + ext;
            File pathname = new File(filePath + newfile);
            file.transferTo(pathname);

            return publicNetworkIp+newfile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
