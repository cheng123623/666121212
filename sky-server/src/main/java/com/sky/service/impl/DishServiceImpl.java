package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void save(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(f -> f.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        List<Dish> list = dishMapper.pageQuery(dto);
        List<DishVO> voList = new ArrayList<>();
        for (Dish dish : list) {
            DishVO vo = new DishVO();
            BeanUtils.copyProperties(dish, vo);
            Category category = categoryMapper.getById(dish.getCategoryId());
            if (category != null) vo.setCategoryName(category.getName());
            voList.add(vo);
        }
        return new PageResult(voList.size(), voList);
    }

    @Override
    @Transactional
    public void update(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.update(dish);
        dishFlavorMapper.deleteByDishId(dto.getId());
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(f -> f.setDishId(dto.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        dishMapper.updateStatus(status, id);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO vo = new DishVO();
        BeanUtils.copyProperties(dish, vo);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        vo.setFlavors(flavors);
        Category category = categoryMapper.getById(dish.getCategoryId());
        if (category != null) vo.setCategoryName(category.getName());
        return vo;
    }

    @Override
    public List<DishVO> list(Long categoryId) {
        List<Dish> dishes = dishMapper.getByCategoryId(categoryId);
        List<DishVO> voList = new ArrayList<>();
        for (Dish dish : dishes) {
            DishVO vo = new DishVO();
            BeanUtils.copyProperties(dish, vo);
            vo.setFlavors(dishFlavorMapper.getByDishId(dish.getId()));
            voList.add(vo);
        }
        return voList;
    }
}
