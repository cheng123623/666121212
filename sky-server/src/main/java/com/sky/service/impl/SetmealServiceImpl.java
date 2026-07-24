package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void save(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        setmealMapper.insert(setmeal);
        List<SetmealDish> dishes = dto.getSetmealDishes();
        if (dishes != null && !dishes.isEmpty()) {
            dishes.forEach(d -> d.setSetmealId(setmeal.getId()));
            setmealDishMapper.insertBatch(dishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto) {
        List<Setmeal> list = setmealMapper.pageQuery(dto);
        List<SetmealVO> voList = new ArrayList<>();
        for (Setmeal s : list) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(s, vo);
            Category c = categoryMapper.getById(s.getCategoryId());
            if (c != null) vo.setCategoryName(c.getName());
            voList.add(vo);
        }
        return new PageResult(voList.size(), voList);
    }

    @Override
    @Transactional
    public void update(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(dto.getId());
        List<SetmealDish> dishes = dto.getSetmealDishes();
        if (dishes != null && !dishes.isEmpty()) {
            dishes.forEach(d -> d.setSetmealId(dto.getId()));
            setmealDishMapper.insertBatch(dishes);
        }
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == 1) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal == null) throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        setmealMapper.updateStatus(status, id);
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal s = setmealMapper.getById(id);
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(s, vo);
        vo.setSetmealDishes(setmealDishMapper.getBySetmealId(id));
        Category c = categoryMapper.getById(s.getCategoryId());
        if (c != null) vo.setCategoryName(c.getName());
        return vo;
    }

    @Override
    public List<SetmealVO> list(Long categoryId) {
        List<Setmeal> list = setmealMapper.getByCategoryId(categoryId);
        List<SetmealVO> voList = new ArrayList<>();
        for (Setmeal s : list) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(s, vo);
            vo.setSetmealDishes(setmealDishMapper.getBySetmealId(s.getId()));
            voList.add(vo);
        }
        return voList;
    }
}
