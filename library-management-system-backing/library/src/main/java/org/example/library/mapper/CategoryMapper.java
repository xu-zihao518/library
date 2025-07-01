package org.example.library.mapper;

import org.apache.ibatis.annotations.*;
import org.example.library.pojo.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("INSERT into category(category_name,usercreate_ID,create_time)"
            +"values(#{categoryName},#{usercreateID},#{createTime})")
    void add(Category category);

    @Select("select* from category")
    List<Category> list();

    @Select("select * from category where categoryId=#{categoryId}")
    Category findById(Integer categoryId);



    @Update("update category set category_name=#{categoryName}where categoryId=#{categoryId}")
    void update(Category category);

    @Delete("Delete from category where categoryId=#{categoryId} and category_name=#{categoryName}")
    void delete(Category category);
}
