package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟检查一次，超时15分钟未支付自动取消
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时检查超时未支付订单...");
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(1, time);
        if (orders != null) {
            for (Orders order : orders) {
                order.setStatus(6);
                order.setCancelReason("订单超时未支付，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("自动取消超时订单: {}", order.getNumber());
            }
        }
    }

    /**
     * 每天凌晨1点，自动完成派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("定时处理派送完成订单...");
        LocalDateTime time = LocalDateTime.now().minusHours(2);
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(4, time);
        if (orders != null) {
            for (Orders order : orders) {
                order.setStatus(5);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("自动完成订单: {}", order.getNumber());
            }
        }
    }
}
