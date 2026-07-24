package com.sky.service.impl;

import com.sky.service.WeChatPayService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeChatPayServiceImpl implements WeChatPayService {

    @Override
    public Map<String, String> createPayQrCode(String orderNumber, String totalAmount, String description) {
        // 模拟微信支付下单，返回二维码链接
        Map<String, String> result = new HashMap<>();
        result.put("code_url", "weixin://wxpay/bizpayurl?pr=" + orderNumber);
        result.put("order_number", orderNumber);
        result.put("total_amount", totalAmount);
        return result;
    }
}
