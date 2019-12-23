package com.qianfeng.v17order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.v17order.pojo.AlipayBizContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author pangzhenyu
 * @Date 2019/11/18
 */
@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    private AlipayClient alipayClient;

    @RequestMapping("generate")
    public void generate(String orderId, HttpServletResponse httpResponse) throws IOException {
        /*AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016101700706635",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDbv3ftUaUgdft873iJ7gO98oMHGUDJXHlbnLYkv0C7YFxTBklW+0Jvo+0xybdmCSBZV7naMvfRMnQMq15N7ZxHro6RFotPuRnzTBpLqECL0M42TkjaO2ihQw003VqqTxjlbIErjK7F1DuabpC9f+8yahFRsLAKUT27oyqGhNzYCIb3ohALDGq1dccRq6TOZCQCU2EBzD3UzlBR9s6Jw/Cy7ceQ1q0qFl4aZDwGQqsdqC7oP8TsjIc0nMb1OF99yjU3ieHe0Fptuh9qzzyPWfFzDP7Ur/w/WDwoDyG/6p6s5JU1WD7JjIoY/07Ugxnu+3nC2v28byw6/8Kvz14/e/EfAgMBAAECggEAU0M5LbLB3orVv4D9K0YEJPS+sPHgmmtvkEbQvRu6a0UXtl2rbEnH4oU6WqMT2pWarD+RLBwjyiBT2HkWQnDFxmNdoZvpLUv1iTpMvF4EFvYHLIx9DjN0caX2WxGESQOvaz6xss6baloAO4OpVCQlffSym0FXcpFV0sMAWff9tdzRP4O7lhdSJfR1WyIT8BtU4PL69hAC2NrDXaSq3dbLn8jhlz07HTZmBssMlSqMb65TNyw5JLcU3DTRAgDPWExHReCvbKHerPppD3feWM26jLy2LzKQXs735jukA5pOgbS8FMOcTVAKkatbk+7Q5SdaoFvxlbsTZHrvI5AZbZTwAQKBgQD7CEnxzCbHAByasrJ4Y3PEQahow/Ct4pOsgqDCtjUpT2DNQQ9UZH6VovYUPE4yRN3pIwp47onYBEOfRMOKvwxgD3J2+72eRY9jB4JRTnqblLvMb16/iBx4fNipGhAMKTKUWfL8wsBJzSUlpi1dtXUzt3kVcXSAHMABmB4YSKFOqQKBgQDgGLHmjomDaBWXwbNo7mkQnZ4lWCLko2iI1i5e70B+6J4eROd8ybN2BXCYSBEoFOKnsWuPxsAchArVT0hyLU1Xm82hgzmL1sDxEc5tD5fgz+RM/qEToSMeLABDNl2vQa66KUEfivWSbEkzFdC8rbpmS3rLaN6Je8rEvKYWe6eGhwKBgQCZYlssLz2SYnMX4xBBlyLpS+UG/pJU7RimU7q+vm6FK0dOou4m9rCR5dzOSHrPJF0jUpHXiokXQKPA8Vc5s7pwB4A9S1x1FLOkIrZxz64C2kJVi8cs6JHc3QvVZ2Neyt1o3/0kjCrJ7VtBQeR+WZ2fV//YJla04NxBHR1mYRqO4QKBgF8iXLPSIxLEJxaLxsxvGe9R2odkCTwjGwesVXc7/pdhzW5wNg3F2wfpe0lKnu3zH92s9krNR1VTwP2ZcNn33XOp0vwUJ2P0QWiV9JpcGk/4MaV2G4+nwq7WYy4lcFQsgC7LwLfRWDtW+SRUyI6w+gbfWDvuQzr+cLBgnenBiPcVAoGAd6Cxd8FudunSaZtsmtDDt8qZiIljyt/+u/jainilhYuPTA27uJbI5kbky/Ws5EOpmkeEUti7bvB2Q4RHluKz55n7zGZsjgakij+PfY12HMI9Nq51BzDgaGxVOGleMyJhTOOBtjcFc1saTKpQkE/2p7WQMzWyTUK8BlVNYNFWFMo=",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu4ADPGg6m3gmK/uf1KRjRXZxKWJxo6TwXI43W9SzX1jRJOMrT8RPY77H4olFbxEsfXbazpHH4NrxS3Ac20W/SbxEkR4BJpBrsCYlME3Ceu0HU2mm2QbqIvD+tX3BAc1UEizdmk5OAoiT7+9UM9LPxOBi8rOkggL5O6dG/U2Jg6G4WPR9dgw0qgTrbubg+ryez9ucspGVG7Oiu7v35GyVxACpuXOtM3NAxf3zezuIRAVnyFXVUBB8q+vnGUQQClFD+BKG7zRNiCZMwuTSUaFGEe2RZDPOP+m9xr//wvySEwWnJds4q/ToP89OR1lsX+9dX5j8Ki253EDA4EBByfVXpwIDAQAB",
                "RSA2");*/ //获得初始化的AlipayClient
        //创建交易请求对象，用于封装业务相关的参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("https://www.baidu.com");//同步跳转，给用户看的
        alipayRequest.setNotifyUrl("http://e3ravk.natappfree.cc/pay/notifyPay");//异步跳转，我们来判断是否是支付宝回调

        AlipayBizContent alipayBizContent = new AlipayBizContent(
                orderId,"FAST_INSTANT_TRADE_PAY","888","IphoneX 100G","IphoneX 16G");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(alipayBizContent);
        alipayRequest.setBizContent(json);//填充业务参数
        /*alipayRequest.setBizContent("{" +
                //out_trade_no订单编号
                "    \"out_trade_no\":\""+orderId+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":8888.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数*/
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
            e.printStackTrace();
            }
        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping("notifyPay")
    @ResponseBody
    public void notifyPay(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        System.out.println("支付宝回调");
        Map<String, String> paramsMap = new HashMap<>(); //将异步通知中收到的所有参数都存放到map中
        //将所有请求参数都放到request对象
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            String[] value = entry.getValue();
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<value.length-1;i++){
                sb.append(value[i]+",");
            }
            sb.append(value[value.length-1]);
            paramsMap.put(entry.getKey(),sb.toString());
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu4ADPGg6m3gmK/uf1KRjRXZxKWJxo6TwXI43W9SzX1jRJOMrT8RPY77H4olFbxEsfXbazpHH4NrxS3Ac20W/SbxEkR4BJpBrsCYlME3Ceu0HU2mm2QbqIvD+tX3BAc1UEizdmk5OAoiT7+9UM9LPxOBi8rOkggL5O6dG/U2Jg6G4WPR9dgw0qgTrbubg+ryez9ucspGVG7Oiu7v35GyVxACpuXOtM3NAxf3zezuIRAVnyFXVUBB8q+vnGUQQClFD+BKG7zRNiCZMwuTSUaFGEe2RZDPOP+m9xr//wvySEwWnJds4q/ToP89OR1lsX+9dX5j8Ki253EDA4EBByfVXpwIDAQAB",
                "utf-8",
                "RSA2"); //调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("支付宝验签成功");
            //判断支付是否成功
            String trade_status = request.getParameter("trade_status");
            if("STRADE_SUCCESS".equals(trade_status)){
                //跟我们的订单数据作比较，匹配成功则将订单状态改为已支付
                String trade_no = request.getParameter("trade_no");
                String app_id = request.getParameter("app_id");
                String out_trade_no = request.getParameter("out_trade_no");
                String buyer_id = request.getParameter("buyer_id");
                String seller_id = request.getParameter("seller_id");
                String total_amount = request.getParameter("total_amount");
                String receipt_amount = request.getParameter("receipt_amount");

                response.getWriter().write("success");
                response.getWriter().flush();
                response.getWriter().close();
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("支付宝验签失败");
            response.getWriter().write("failure");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
