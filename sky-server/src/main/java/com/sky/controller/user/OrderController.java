package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderVO> submit(@RequestBody OrdersSubmitDTO dto) {
        return Result.success(orderService.submit(dto));
    }

    @GetMapping("/historyOrders")
    public Result<List<OrderVO>> historyOrders(Integer status) {
        return Result.success(orderService.historyOrders(status));
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        return Result.success(orderService.orderDetail(id));
    }

    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        orderService.userCancel(id);
        return Result.success();
    }
}
