package com.qianfeng.api;

import com.qianfeng.base.IBaseService;
import com.qianfeng.entity.TUser;
import com.qianfeng.result.ResultBean;

/**
 * @Author pangzhenyu
 * @Date 2019/11/11
 */
public interface IUserService extends IBaseService<TUser> {

    public ResultBean checkUserNameIsExists(String username);

    public ResultBean checkPhoneIsExists(String phone);

    public ResultBean checkEmailIsExists(String email);

    public ResultBean generateCode(String identification);

    ResultBean checkByIdentification(TUser user);

    ResultBean checkIsLogin(String value);

    ResultBean register(TUser user);
}
