package org.example.library.service.impl;

import org.example.library.mapper.CategoryMapper;
import org.example.library.pojo.Category;
import org.example.library.service.CategoryService;
import org.example.library.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServieImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());

        Map<String,Object> map=ThreadLocalUtil.get();
        String usercreateID=(String) map.get("userId");
        category.setUsercreateID(usercreateID);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        return categoryMapper.list();
    }

    @Override
    public Category findById(Integer categoryId) {
       Category c=categoryMapper.findById(categoryId);
       return c;
    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void delete(Category category) {

        categoryMapper.delete(category);
    }

}
