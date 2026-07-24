package com.sky.controller.admin;

import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.SalesTop10ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/orderStatistics")
    public Result<OrderStatisticsVO> orderStatistics() {
        OrderStatisticsVO vo = OrderStatisticsVO.builder()
                .toBeConfirmed(orderMapper.countByStatus(2))
                .confirmed(orderMapper.countByStatus(3))
                .deliveryInProgress(orderMapper.countByStatus(4))
                .build();
        return Result.success(vo);
    }

    @GetMapping("/turnoverStatistics")
    public Result<BusinessDataVO> turnoverStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        Double turnover = orderMapper.turnoverStatistics(begin, end);
        Integer validCount = orderMapper.validOrderCount(begin, end);
        Integer totalCount = orderMapper.totalOrderCount(begin, end);
        Integer newUsers = orderMapper.newUserCount(begin, end);
        BusinessDataVO vo = BusinessDataVO.builder()
                .turnover(turnover != null ? turnover : 0.0)
                .validOrderCount(validCount != null ? validCount : 0)
                .newUsers(newUsers != null ? newUsers : 0)
                .orderCompletionRate(totalCount != null && totalCount > 0 ? (double) validCount / totalCount : 0.0)
                .unitPrice(turnover != null && validCount != null && validCount > 0 ? turnover / validCount : 0.0)
                .build();
        return Result.success(vo);
    }

    @GetMapping("/userStatistics")
    public Result<BusinessDataVO> userStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        Integer newUsers = orderMapper.newUserCount(begin, end);
        return Result.success(BusinessDataVO.builder().newUsers(newUsers != null ? newUsers : 0).build());
    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10() {
        return Result.success(SalesTop10ReportVO.builder().nameList("").numberList("").build());
    }
}
