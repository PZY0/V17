package com.qianfeng.api;

import com.qianfeng.result.ResultBean;

/**
 * @Author pangzhenyu
 * @Date 2019/11/2
 */
public interface ISearchService {
    //全量同步
    public ResultBean synAll();

    //增量同步
    public ResultBean synById(Long id);

    //删除
    public ResultBean delById(Long id);

    //查询
    public ResultBean queryByKeywords(String keywords);

    //分页查询
    ResultBean queryByKeywords(String keywords, Integer pageIndex, Integer pageSize);
}
