package com.qianfeng.v17order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pangzhenyu
 * @Date 2019/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlipayBizContent {

    private String out_trade_no;
    //平台与支付宝签约的商品类型，比如快捷支付
    private String product_code;

    private String total_amount;

    private String subject;

    private String body;

    /*private String passback_params;

    private String extend_params;

    private String sys_service_provider_id;*/

}
