package com.qianfeng.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/10/29
 */
public interface IBaseService<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKeyWithBLOBs(T t);

    int updateByPrimaryKey(T t);

    List<T> selectAll();

    PageInfo<T> pageList(Integer pageIndex, Integer pageSize);
}
