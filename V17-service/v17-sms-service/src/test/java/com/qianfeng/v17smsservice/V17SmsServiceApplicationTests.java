package com.qianfeng.v17smsservice;

import com.qianfeng.api.ISMS;
import com.qianfeng.pojo.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class V17SmsServiceApplicationTests {

    @Autowired
    private ISMS isms;

    @Test
    void sendMessageTest() {
        MessageResponse messageResponse = isms.sendSMS("18878478823", "123456");
        System.out.println(messageResponse.getCode());
    }

}
