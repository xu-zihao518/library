package org.example.library.service.impl;

import org.example.library.mapper.BookMapper;
import org.example.library.pojo.Book;
import org.example.library.pojo.Category;
import org.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> list() {
       return bookMapper.list();
    }

    @Override
    public void add(Book book) {
        bookMapper.add(book);
    }
    @Override
    public void delete(Book book) {

        bookMapper.delete(book);
    }
}
