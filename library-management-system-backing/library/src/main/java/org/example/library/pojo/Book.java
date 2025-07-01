package org.example.library.pojo;


import lombok.Data;

import java.time.LocalDate;

@Data
public class Book {
    private String bookId;
    private String bookISBN;
    private String bookName;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private String category;
    private int count;
    private String description;
}
