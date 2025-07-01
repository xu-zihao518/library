package org.example.library.controller;

import org.example.library.pojo.Category;
import org.example.library.pojo.Result;
import org.example.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping()
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return Result.success();
    }
    @GetMapping()
    public Result<List<Category>> findAll(){
        List<Category> cs=categoryService.list();
        return Result.success(cs);
    }
    @GetMapping("/detail")
    public Result<Category> detail(Integer categoryId){
        Category c=categoryService.findById(categoryId);
        return Result.success(c);
    }
    @PutMapping()
    public Result update(@RequestBody @Validated Category category){
        categoryService.update(category);
        return Result.success();
    }
    @DeleteMapping()
    public Result delete(@RequestBody @Validated Category category){
        categoryService.delete(category);
        return Result.success();
    }
}
