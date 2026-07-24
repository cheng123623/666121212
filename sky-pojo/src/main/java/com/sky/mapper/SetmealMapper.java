package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface SetmealMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<Setmeal> pageQuery(SetmealPageQueryDTO dto);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @Select("select * from setmeal where category_id = #{categoryId} and status = 1")
    List<Setmeal> getByCategoryId(Long categoryId);
}
