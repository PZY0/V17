package com.qianfeng.mapper;

import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser> {
    TUser selectByIdentification(String username);
}