package com.qianfeng.v17center.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qianfeng.MQConstant.MQConstant;
import com.qianfeng.api.IProductService;
import com.qianfeng.entity.TProduct;
import com.qianfeng.result.ResultBean;
import com.qianfeng.vo.ProductVO;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/10/28
 */
@Controller
@RequestMapping("product")
public class ProductController {

    //启动时检查机制
    @Reference(check = false)
    private IProductService productService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable Long id){
        return productService.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String list(ModelMap map){
        List<TProduct> products = productService.selectAll();
        map.put("list",products);
        return "product/list";
    }

    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String pageList(@PathVariable Integer pageIndex,@PathVariable Integer pageSize,ModelMap map){
        PageInfo<TProduct> page = productService.pageList(pageIndex, pageSize);
        for (TProduct product : page.getList()) {
            System.out.println(product.getName());
        }
        map.put("page",page);
        return "product/list";
    }

    /*@PostMapping("add")
    public String add(ProductVO productVO){
        Long add = productService.add(productVO);
        return "redirect:/product/page/1/2";
    }*/
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                System.out.println("correlationData: " + correlationData.getId());
            }
        }
    };
    @PostMapping("add")
    public String add(ProductVO productVO){
        Long add = productService.add(productVO);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData(add.toString());
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,"product.update",add,correlationData);
        return "redirect:/product/page/1/3";
    }

    @RequestMapping("del/{id}")
    public String del(@PathVariable int id){
        return "redirect:/product/page/1/3";
    }

    @GetMapping("toUpdate")
    @ResponseBody
    public ProductVO toUpdate(Long id){
        ProductVO productVO = productService.selectById(id);
        return productVO;
    }

    @PostMapping("update")
    public String update(ProductVO productVO){
        System.out.println(productVO.getProductdesc());
        productService.update(productVO);
        //发送消息到交换机
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,"product.update",productVO.getProduct().getId());
        return "redirect:/product/page/1/3";
    }

    @PostMapping("del")
    @ResponseBody
    public ResultBean del(Long id){
        int count = productService.deleteByPrimaryKey(id);
        if(count>0){
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,"product.update",""+id);
            return new ResultBean(200,"删除成功");
        }
        return new ResultBean(500,"删除失败");
    }

    @PostMapping("batchDel")
    @ResponseBody
    public ResultBean batchid(@RequestParam List<Long> ids){
        int count = productService.batchDel(ids);
        if(count>0){
            return new ResultBean(200,"删除成功");
        }
        return new ResultBean(500,"删除失败");
    }
}
