package org.example.library.service;

import org.example.library.pojo.Category;

import java.util.List;

public interface CategoryService {
//    新增分类
    void add(Category category);

    List<Category> list();

    Category findById(Integer categoryId);

    void update(Category category);

    void delete(Category category);
}
