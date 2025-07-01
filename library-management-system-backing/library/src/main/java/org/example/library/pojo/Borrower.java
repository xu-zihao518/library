package org.example.library.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Borrower {
    private int borrowId;
    private String userId;
    private String bookname;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
}
