package com.qianfeng.v17springbootemail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootTest
class V17SpringbootEmailApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void txtTest() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("zy78478823@163.com");
        simpleMailMessage.setTo("18878478823@139.com");
        simpleMailMessage.setSubject("邮件测试");
        simpleMailMessage.setText("这是一封测试邮件");

        javaMailSender.send(simpleMailMessage);
    }

    @Test
    void HTMLTest() {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom("zy78478823@163.com");
            helper.setTo("18878478823@139.com");
            helper.setSubject("还是一封测试邮件");
            helper.setText("<a href='https://www.baidu.com'>进入百度</a>",true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void templatesTest() {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom("zy78478823@163.com");
            helper.setTo("18878478823@139.com");
            helper.setSubject("还是一封测试邮件");
            Context context = new Context();
            context.setVariable("name","张三");
            String content = templateEngine.process("birthday", context);
            helper.setText(content,true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
