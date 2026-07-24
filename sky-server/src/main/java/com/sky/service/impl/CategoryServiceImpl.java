package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void save(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setStatus(StatusConstant.ENABLE);
        categoryMapper.insert(category);
    }

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void update(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        categoryMapper.update(category);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryMapper.getById(id);
        if (category.getType() == 1) {
            if (dishMapper.getByCategoryId(id) != null && !dishMapper.getByCategoryId(id).isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
        } else {
            if (setmealMapper.getByCategoryId(id) != null && !setmealMapper.getByCategoryId(id).isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.update(category);
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.getByType(type);
    }
}
