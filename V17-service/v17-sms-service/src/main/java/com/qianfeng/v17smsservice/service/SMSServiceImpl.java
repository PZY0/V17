package com.qianfeng.v17smsservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.qianfeng.api.ISMS;
import com.qianfeng.pojo.MessageResponse;

/**
 * @Author pangzhenyu
 * @Date 2019/11/12
 */
@Service
public class SMSServiceImpl implements ISMS {
    @Override
    public MessageResponse sendSMS(String phone, String code) {
        //凭证
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Fkym4495AKnFCSP35mb", "0cDExRubThwQyJFcQn807a08bVf9sn");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();

        request.setSysAction("SendSms");
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Java交流平台");
        request.putQueryParameter("TemplateCode", "SMS_177257853");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            Gson gson = new Gson();
            return gson.fromJson(response.getData(), MessageResponse.class);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
