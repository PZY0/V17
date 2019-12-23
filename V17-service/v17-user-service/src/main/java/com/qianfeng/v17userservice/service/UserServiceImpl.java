package com.qianfeng.v17userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.api.IUserService;
import com.qianfeng.base.BaseServiceImpl;
import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TUser;
import com.qianfeng.mapper.TUserMapper;
import com.qianfeng.result.ResultBean;
import com.qianfeng.util.CodeUtils;
import com.qianfeng.v17userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/11
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean checkUserNameIsExists(String username) {
        return null;
    }

    @Override
    public ResultBean checkPhoneIsExists(String phone) {
        return null;
    }

    @Override
    public ResultBean checkEmailIsExists(String email) {
        return null;
    }

    @Override
    public ResultBean generateCode(String identification) {
        //生成验证码
        String code = CodeUtils.generateCode(6);
        //在redis存放唯一凭证和验证码的key-value数据
        redisTemplate.opsForValue().set(identification,code,5, TimeUnit.MINUTES);
        //异步发送消息
        Map<String,String> map = new HashMap<>();
        map.put("identification",identification);
        map.put("code",code);
        rabbitTemplate.convertAndSend("sms_exchange","sms.code",map);
        return new ResultBean(200,"发送成功");
    }

    //cookie+redis:有状态方式
    /*@Override
    public ResultBean checkByIdentification(TUser user) {
        TUser user2 = userMapper.selectByIdentification(user.getUsername());
        if (user2!=null && user.getPassword().equals(user2.getPassword())){
            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("user-token-"+uuid,user2.getUsername(),30,TimeUnit.MINUTES);
            return new ResultBean(200,uuid);
        }
        return new ResultBean(404,user.getUsername());
    }*/
    //cookie+jwt：无状态方式
    @Override
    public ResultBean checkByIdentification(TUser user) {
        TUser user2 = userMapper.selectByIdentification(user.getUsername());
        //if (user2!=null && user.getPassword().equals(user2.getPassword())){
        if(user2!=null && passwordEncoder.matches(user.getPassword(),user2.getPassword())){
            JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setSecretKey("java1907");
            jwtUtils.setTtl(30*60*1000);
            String jwtToken = jwtUtils.createJwtToken(user2.getId().toString(), user2.getUsername());

            //返回用户名和唯一标识
            Map<String,String> map = new HashMap<>();
            map.put("jwtToken",jwtToken);
            map.put("username",user2.getUsername());
            return new ResultBean(200,map);
        }
        return new ResultBean(404,null);
    }

    /*@Override
    public ResultBean checkIsLogin(String value) {
        StringBuilder key = new StringBuilder("user-token-").append(value);
        if(redisTemplate.hasKey(key.toString())){
            redisTemplate.expire(key.toString(),30,TimeUnit.MINUTES);
            return new ResultBean(200,"已登陆");
        }
        return new ResultBean(404,null);
    }*/
    @Override
    public ResultBean checkIsLogin(String value) {
        try {
            JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setSecretKey("java1907");
            Claims claims = jwtUtils.parseJwtToken(value);
            String username = claims.getSubject();
            return new ResultBean(200,username);
        }catch (RuntimeException e){
            return new ResultBean(404,null);
        }
    }

    @Override
    public ResultBean register(TUser user) {
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setFlag(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int result = userMapper.insertSelective(user);
        if(result > 0){
            return new ResultBean(200,user.getUsername());
        }
        return new ResultBean(500,null);
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
