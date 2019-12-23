package com.qianfeng.v17center;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class V17CenterApplicationTests {

    @Autowired
    private FastFileStorageClient client;

    @Test
    void contextLoads() throws FileNotFoundException {
        File file = new File("D:\\lwl\\V17\\V17-web\\v17-center\\src\\main\\resources\\static\\TT.jpg");
        FileInputStream fis = new FileInputStream(file);
        StorePath path = client.uploadImageAndCrtThumbImage(fis, file.length(), "jpg", null);
        System.out.println(path.getFullPath());
        System.out.println(path.getGroup());
        System.out.println(path.getPath());
    }

    @Test
    void del(){
        client.deleteFile("group1/M00/00/01/CiQICF26x32ARAb9AAAGhFrCTbU227.jpg");
        System.out.println("ok");
    }

}
