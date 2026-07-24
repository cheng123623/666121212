package com.sky.controller.admin;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Value("${sky.alioss.endpoint:}")
    private String endpoint;
    @Value("${sky.alioss.access-key-id:}")
    private String accessKeyId;
    @Value("${sky.alioss.access-key-secret:}")
    private String accessKeySecret;
    @Value("${sky.alioss.bucket-name:}")
    private String bucketName;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID().toString() + extension;

            if (endpoint != null && !endpoint.isEmpty() && !"your-access-key-id".equals(accessKeyId)) {
                // 使用阿里云 OSS 上传
                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                ossClient.putObject(bucketName, filename, new ByteArrayInputStream(file.getBytes()));
                ossClient.shutdown();
                String url = "https://" + bucketName + "." + endpoint + "/" + filename;
                return Result.success(url);
            } else {
                // 本地存储
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();
                file.transferTo(new File(UPLOAD_DIR + filename));
                return Result.success(filename);
            }
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
