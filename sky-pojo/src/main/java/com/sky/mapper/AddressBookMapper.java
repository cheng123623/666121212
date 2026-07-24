package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface AddressBookMapper {

    @Select("select * from address_book where user_id = #{userId} order by is_default desc")
    List<AddressBook> getByUserId(Long userId);

    void insert(AddressBook addressBook);

    void update(AddressBook addressBook);

    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void setDefaultToZero(Long userId);
}
