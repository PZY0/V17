package com.qianfeng.v17index.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.IProductTypeService;
import com.qianfeng.entity.TProductType;
import com.qianfeng.result.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/11/1
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @Reference
    private IProductTypeService productTypeService;

    //@Resource(name = "redisTemplate_s")
    /*@Autowired
    private RedisTemplate redisTemplate;*/

    /*@RequestMapping("show")
    public String show(Model model){
        List<TProductType> productTypes = productTypeService.selectAll();
        model.addAttribute("list",productTypes);
        return "index";
    }*/
    /*@RequestMapping("show")
    public String show(Model model){
        if(redisTemplate.opsForList().size("id")==0){
            List<TProductType> productTypes = productTypeService.selectAll();
            for (TProductType productType : productTypes) {
                redisTemplate.opsForList().leftPush("id",String.valueOf(productType.getId()));
                redisTemplate.opsForList().leftPush("name",productType.getName());
                redisTemplate.opsForList().leftPush("pid",String.valueOf(productType.getPid()));
            }
            model.addAttribute("list",productTypes);
        }else {
            List id = redisTemplate.opsForList().range("id", 0, -1);
            List name = redisTemplate.opsForList().range("name", 0, -1);
            List pid = redisTemplate.opsForList().range("pid", 0, -1);
            List<TProductType> list = new ArrayList<>(id.size());
            for (int i = 0; i < id.size(); i++) {
                TProductType productType = new TProductType();
                productType.setId(Integer.parseInt((String) id.get(i)));
                productType.setName((String) name.get(i));
                productType.setPid(Integer.parseInt((String) pid.get(i)));
                list.add(productType);
            }
            model.addAttribute("list",list);
        }
        return "index";
    }*/
    @RequestMapping("show")
    public String show(Model model){
        List<TProductType> productTypes = productTypeService.selectAll();
        model.addAttribute("list",productTypes);
        return "index";
    }

    @GetMapping("showtype")
    @ResponseBody
    public ResultBean showType(){
        List<TProductType> productTypes = productTypeService.selectAll();
        return new ResultBean(200,productTypes);
    }
}
