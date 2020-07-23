package com.qianfeng.v17center.filecontroller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qianfeng.result.ResultBean;
import com.qianfeng.v17center.pojo.EditorResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author pangzhenyu
 * @Date 2019/11/1
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${images.server}")
    private String imageServer;

    @PostMapping("upload")
    public ResultBean upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), extName, null);
            String fullPath = storePath.getFullPath();
            StringBuilder sb = new StringBuilder(imageServer).append(fullPath);
            return new ResultBean(200, sb);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean(500, "文件上传失败");
        }
    }

    @PostMapping("uploads")
    public EditorResultBean uploads(MultipartFile[] files) {
        String[] data = new String[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                String originalFilename = files[i].getOriginalFilename();
                String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                StorePath storePath = client.uploadImageAndCrtThumbImage(
                        files[i].getInputStream(), files[i].getSize(), substring, null);
                StringBuilder sb = new StringBuilder(imageServer).append(storePath.getFullPath());
                data[i] = sb.toString();
            }
            return new EditorResultBean("0", data);
        } catch (IOException e) {
            e.printStackTrace();
            return new EditorResultBean("1", null);
        }
    }
}
