package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import java.util.List;

public interface DishService {
    void save(DishDTO dto);
    PageResult pageQuery(DishPageQueryDTO dto);
    void update(DishDTO dto);
    void deleteBatch(List<Long> ids);
    void startOrStop(Integer status, Long id);
    DishVO getById(Long id);
    List<DishVO> list(Long categoryId);
}
