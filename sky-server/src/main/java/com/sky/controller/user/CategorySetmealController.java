package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.list(type));
    }
}

@RestController
@RequestMapping("/user/setmeal")
class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/list")
    public Result<List<SetmealVO>> list(Long categoryId) {
        return Result.success(setmealService.list(categoryId));
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        return Result.success(setmealService.getById(id));
    }
}
