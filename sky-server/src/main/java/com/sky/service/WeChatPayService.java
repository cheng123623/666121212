package com.sky.service;

import java.util.Map;

public interface WeChatPayService {
    /**
     * 生成支付二维码（模拟）
     */
    Map<String, String> createPayQrCode(String orderNumber, String totalAmount, String description);
}
