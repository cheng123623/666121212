package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    List<Dish> pageQuery(DishPageQueryDTO dto);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> getByCategoryId(Long categoryId);
}
