package com.qianfeng.v17search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.ISearchService;
import com.qianfeng.result.PageResultBean;
import com.qianfeng.result.ResultBean;
import com.qianfeng.vo.ProductPartVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author pangzhenyu
 * @Date 2019/11/3
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("synAll")
    @ResponseBody
    public ResultBean synAll(){
        return searchService.synAll();
    }

    //此接口适用于app端或Ajax异步加载数据的方式
    @RequestMapping("queryByKeyWords")
    public ResultBean queryKeyWords(String keywords){
        return searchService.queryByKeywords(keywords);
    }

    //此接口适用于pc端
    @RequestMapping("queryByKeyWords4PC/{pageIndex}/{pageSize}")
    public String queryKeyWords4PC(String keywords, @PathVariable Integer pageIndex,@PathVariable Integer pageSize, ModelMap map){
        ResultBean resultBean = searchService.queryByKeywords(keywords,pageIndex,pageSize);
        //List<ProductPartVO> list = (List<ProductPartVO>) resultBean.getData();
        PageResultBean<ProductPartVO> data = (PageResultBean<ProductPartVO>) resultBean.getData();
        map.put("page",data);
        return "list";
    }
}
