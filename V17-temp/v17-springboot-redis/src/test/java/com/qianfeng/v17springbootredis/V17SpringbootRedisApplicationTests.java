package com.qianfeng.v17springbootredis;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qianfeng.api.IProductTypeService;
import com.qianfeng.entity.TProductType;
import com.qianfeng.v17springbootredis.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class V17SpringbootRedisApplicationTests {

    /*@Resource(name = "redisTemplate_s")
    private RedisTemplate<String,Object> redisTemplate;*/
    @Autowired
    private  RedisTemplate redisTemplate;

    @Reference
    private IProductTypeService productTypeService;

    @Test
    void contextLoads() {
        /*redisTemplate.opsForValue().set("k1","v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));*/
        System.out.println(redisTemplate.opsForList().size("id"));
    }

    @Test
    void hashTest(){
        /*System.out.println(redisTemplate.hasKey("producttype"));
        Object name = redisTemplate.opsForHash().get("producttype", "name");*/
        /*System.out.println(redisTemplate.opsForList().size("id"));
        System.out.println(redisTemplate.opsForList().size("name"));
        System.out.println(redisTemplate.opsForList().size("pid"));
        List id = redisTemplate.opsForList().range("id", 0,-1);
        System.out.println(id);*/

        List id = redisTemplate.opsForList().range("id", 0, -1);
        List name = redisTemplate.opsForList().range("name", 0, -1);
        List pid = redisTemplate.opsForList().range("pid", 0, -1);
        List<TProductType> list = new ArrayList<>(id.size());
        for (int i = 0; i < id.size(); i++) {
            TProductType productType = new TProductType();
            //Object o = id.get(1);
            /*productType.setId((Integer) id.get(i));
            productType.setName((String) name.get(i));
            productType.setPid((Integer) pid.get(i));*/
            productType.setId(Integer.parseInt((String) id.get(i)));
            productType.setName((String) name.get(i));
            productType.setPid(Integer.parseInt((String) pid.get(i)));
            list.add(productType);
        }
        System.out.println(list);
    }
    @Test
    public void listTest(){
        List<TProductType> productTypes = productTypeService.selectAll();
        for (TProductType productType : productTypes) {
            redisTemplate.opsForList().leftPush("id",productType.getId());
            redisTemplate.opsForList().leftPush("name",productType.getName());
            redisTemplate.opsForList().leftPush("pid",productType.getPid());
        }
        List<Object> id = redisTemplate.opsForList().range("id", 0, -1);
        List<Object> name = redisTemplate.opsForList().range("name", 0, -1);
        List<Object> pid = redisTemplate.opsForList().range("pid", 0, -1);
        System.out.println("id-->"+id);
        System.out.println("name-->"+name);
        System.out.println("pid-->"+pid);
    }

    @Test
    public void gsonTest(){
        List<TProductType> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TProductType productType = new TProductType();
            productType.setPid(i);
            productType.setId(i);
            productType.setName("k"+i);
            list.add(productType);
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);

    }
    @Test
    public void ssTest(){
        Object obj = redisTemplate.opsForValue().get("producttype");
        System.out.println(obj);
        Gson gson = new Gson();
        List<TProductType> list= gson.fromJson(obj.toString(), new TypeToken<List<TProductType>>() {}.getType());
        //Type type = new TypeToken<List<TProductType>>(){}.getType();
        //System.out.println(list);
        for (TProductType productType : list) {
            System.out.println(productType);
        }
    }

    @Test
    public void cacheTest() throws InterruptedException {
        List<Student> list = (List<Student>) redisTemplate.opsForValue().get("student");
        if(list == null){
            String lock = UUID.randomUUID().toString();
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("lock", lock, 1, TimeUnit.MINUTES);
            if(ifAbsent){
                log.info("查询数据库");
                //Thread.sleep(1);
                list = new ArrayList<>();
                list.add(new Student(1,"zs"));
                list.add(new Student(2,"ls"));
                redisTemplate.opsForValue().set("student",list);
                String currentLock = (String) redisTemplate.opsForValue().get("lock");
                if(lock.equals(currentLock)){
                    redisTemplate.delete("lock");
                }
                return;
            }
        }
        log.info("查询Redis");
    }

    @Test
    public void taskTest(){
        ExecutorService pool = new ThreadPoolExecutor(
                10,20,1,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(100));
        for (int i = 0; i < 50; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        cacheTest();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uu(){

        /*redisTemplate.opsForValue().set("zs","zs");
        Object zs = redisTemplate.opsForValue().get("zs");
        System.out.println(zs);*/
        List<Student> list = new ArrayList<>();
        list.add(new Student(1,"zs"));
        list.add(new Student(2,"ls"));
        redisTemplate.opsForValue().set("student",list);
        System.out.println(redisTemplate.opsForValue().get("student"));
    }
}
