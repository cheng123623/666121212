package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CategoryMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO dto);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    @Select("select * from category where type = #{type}")
    List<Category> getByType(Integer type);

    @Select("select * from category where id = #{id}")
    Category getById(Long id);
}
