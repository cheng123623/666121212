package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO dto);

    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    @Select("select * from orders where number = #{number}")
    Orders getByNumber(String number);

    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    @Select("select sum(amount) from orders where status = 5 and date(order_time) between #{begin} and #{end}")
    Double turnoverStatistics(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Select("select count(*) from orders where status = 5 and date(order_time) between #{begin} and #{end}")
    Integer validOrderCount(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Select("select count(*) from user where date(create_time) between #{begin} and #{end}")
    Integer newUserCount(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Select("select count(*) from orders where date(order_time) between #{begin} and #{end}")
    Integer totalOrderCount(@Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("time") LocalDateTime time);
}
