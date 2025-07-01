package org.example.library.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.library.pojo.Book;
import org.example.library.pojo.Category;
import org.example.library.pojo.Result;
import org.example.library.service.BookService;
import org.example.library.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/book")
public class BookController
{
    @Autowired
    private BookService bookService;
    @GetMapping("")
    public Result<List<Book>> list(){
        List<Book> list = bookService.list();
        return Result.success(list);
    }
    @PostMapping()
    public Result add(@RequestBody Book book){
        bookService.add(book);
        return Result.success();
    }
    @DeleteMapping()
    public Result delete(@RequestBody @Validated Book book){
        bookService.delete(book);
        return Result.success();
    }
}
