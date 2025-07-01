package org.example.library.service;

import org.example.library.pojo.Book;

import java.util.List;

public interface BookService {
    List<Book> list();

    void add(Book book);

    void delete(Book book);
}
