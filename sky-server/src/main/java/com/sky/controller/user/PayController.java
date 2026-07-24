package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.WeChatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user/pay")
public class PayController {

    @Autowired
    private WeChatPayService weChatPayService;

    @GetMapping("/qrCode")
    public Result<Map<String, String>> qrCode(@RequestParam String orderNumber,
                                              @RequestParam String totalAmount) {
        return Result.success(weChatPayService.createPayQrCode(orderNumber, totalAmount, "苍穹外卖订单"));
    }
}
