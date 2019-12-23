package com.qianfeng.v17item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.IProductService;
import com.qianfeng.entity.TProduct;
import com.qianfeng.result.ResultBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author pangzhenyu
 * @Date 2019/11/4
 */
@Controller
@RequestMapping("item")
@Slf4j
public class ItemController {

    @Reference
    private IProductService productService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ThreadPoolExecutor pool;

    @RequestMapping("createById/{id}")
    @ResponseBody
    public ResultBean item(@PathVariable Long id) {
        TProduct product = productService.selectByPrimaryKey(id);
        try {
            //获取模板对象
            Template template = configuration.getTemplate("details.ftl");
            //设置模板数据
            Map<String,TProduct> map = new HashMap<>();
            map.put("product",product);
            //整合模板
            String path = ResourceUtils.getURL("classpath:static").getPath();
            FileWriter fileWriter = new FileWriter(path+ File.separator+id+".html");
            template.process(map,fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean(500,"读取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean(500,"生成静态页面失败");
        }
        return new ResultBean(200,"创建成功");
    }

    @RequestMapping("batch")
    @ResponseBody
    public ResultBean batchById(@PathVariable List<Long> ids){
        List<Future<Long>> list = new ArrayList<>(ids.size());
        for (Long id : ids) {
            Future<Long> submit = pool.submit(new createHTML(id));
            list.add(submit);
        }
        //遍历执行结果
        List<Long> errors = new ArrayList<>();
        for (Future<Long> future : list) {
            try {
                //获取执行结果，阻塞等待
                Long result = future.get();
                if(result!=0){
                    errors.add(result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //遍历执行失败的id
        for (Long error : errors) {
            log.error("出错的id为{}",error);
        }
        return new ResultBean(200,"批量生成页面成功");
    }
    private class createHTML implements Callable<Long>{
        private Long id;

        public createHTML(Long id){
            this.id = id;
        }

        @Override
        public Long call() throws Exception {
            TProduct product = productService.selectByPrimaryKey(id);
            try {
                //获取模板对象
                Template template = configuration.getTemplate("details.ftl");
                //设置模板数据
                Map<String,TProduct> map = new HashMap<>();
                map.put("product",product);
                //整合模板
                String path = ResourceUtils.getURL("classpath:static").getPath();
                FileWriter fileWriter = new FileWriter(path+ File.separator+id+".html");
                template.process(map,fileWriter);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                return id;
            }
            return 0L;
        }
    }
}
