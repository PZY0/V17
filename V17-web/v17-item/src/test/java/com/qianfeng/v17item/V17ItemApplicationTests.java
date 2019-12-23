package com.qianfeng.v17item;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class V17ItemApplicationTests {

    @Autowired
    private Configuration configuration;

    @Test
    void contextLoads() throws IOException, TemplateException {
        //获取模板对象
        Template template = configuration.getTemplate("item.ftl");
        //设置模板数据
        Map<String,Object> map = new HashMap<>();
        map.put("name","freemark");
        //整合
        FileWriter fileWriter = new FileWriter("D:\\lwl\\V17\\V17-web\\v17-item\\src\\main\\resources\\static\\html\\item.html");
        template.process(map,fileWriter);
        System.out.println("整合成功");
    }

    @Test
    void waitAndsleep(){
        and();
    }
    private synchronized void and(){
        System.out.println("开始");
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束");
    }

}
