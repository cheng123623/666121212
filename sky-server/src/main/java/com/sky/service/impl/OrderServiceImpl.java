package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderVO submit(OrdersSubmitDTO dto) {
        List<ShoppingCart> carts = shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        if (carts.isEmpty()) throw new RuntimeException(MessageConstant.SHOPPING_CART_IS_NULL);

        AddressBook address = addressBookMapper.getById(dto.getAddressBookId());
        User user = userMapper.getById(BaseContext.getCurrentId());

        Orders order = new Orders();
        BeanUtils.copyProperties(dto, order);
        order.setNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        order.setStatus(1);
        order.setUserId(BaseContext.getCurrentId());
        order.setOrderTime(LocalDateTime.now());
        order.setPhone(address.getPhone());
        order.setAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getDetail());
        order.setUserName(user.getName());
        order.setConsignee(address.getConsignee());
        orderMapper.insert(order);

        List<OrderDetail> details = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(cart, detail);
            detail.setOrderId(order.getId());
            details.add(detail);
        }
        orderDetailMapper.insertBatch(details);
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

        // WebSocket通知管理端 - 来单提醒
        Map<String, Object> notifyMap = new HashMap<>();
        notifyMap.put("type", 1);
        notifyMap.put("orderId", order.getId());
        notifyMap.put("content", "您有新的订单，请及时处理");
        webSocketServer.sendToAllClient(JSON.toJSONString(notifyMap));

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        return vo;
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(dto);
        List<OrderVO> voList = new ArrayList<>();
        for (Orders o : page.getResult()) {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(o, vo);
            voList.add(vo);
        }
        return new PageResult(page.getTotal(), voList);
    }

    @Override
    public OrderVO getById(Long id) {
        Orders o = orderMapper.getById(id);
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(o, vo);
        return vo;
    }

    @Override
    public void cancel(OrdersCancelDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        order.setStatus(6);
        order.setCancelReason(dto.getCancelReason());
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void confirm(Long id) {
        Orders order = orderMapper.getById(id);
        order.setStatus(3);
        orderMapper.update(order);
    }

    @Override
    public void rejection(OrdersRejectionDTO dto) {
        Orders order = orderMapper.getById(dto.getId());
        order.setStatus(6);
        order.setRejectionReason(dto.getRejectionReason());
        orderMapper.update(order);
    }

    @Override
    public List<OrderVO> historyOrders(Integer status) {
        OrdersPageQueryDTO dto = new OrdersPageQueryDTO();
        dto.setStatus(status);
        List<Orders> list = orderMapper.pageQuery(dto);
        List<OrderVO> voList = new ArrayList<>();
        for (Orders o : list) {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(o, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public OrderVO orderDetail(Long id) {
        Orders o = orderMapper.getById(id);
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(o, vo);
        return vo;
    }

    @Override
    public void userCancel(Long id) {
        Orders order = orderMapper.getById(id);
        order.setStatus(6);
        order.setCancelReason("用户取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }
}
