package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderVO;
import java.util.List;

public interface OrderService {
    OrderVO submit(OrdersSubmitDTO dto);
    PageResult pageQuery(OrdersPageQueryDTO dto);
    OrderVO getById(Long id);
    void cancel(OrdersCancelDTO dto);
    void confirm(Long id);
    void rejection(OrdersRejectionDTO dto);
    List<OrderVO> historyOrders(Integer status);
    OrderVO orderDetail(Long id);
    void userCancel(Long id);
}
