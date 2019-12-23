package com.qianfeng.v17userservice;

import com.qianfeng.api.IUserService;
import com.qianfeng.entity.TUser;
import com.qianfeng.result.ResultBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class V17UserServiceApplicationTests {

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        TUser user = new TUser();
        user.setUsername("12345678910");
        user.setPassword("123");
        ResultBean resultBean = userService.checkByIdentification(user);
        System.out.println(resultBean.getData().toString());
    }

    @Test
    void security(){
        String encode = passwordEncoder.encode("123");
        String e = "$2a$10$EjedKIkY.6XZwqxZ3/RbD.qA0y8kk0mQ/ua5ZOIW4GXTAiSPFSWjO";
        System.out.println(encode);
        System.out.println(passwordEncoder.matches("123", encode));
        System.out.println(passwordEncoder.matches("123", e));
    }
}
