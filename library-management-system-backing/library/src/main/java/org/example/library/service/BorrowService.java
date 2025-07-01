package org.example.library.service;

import org.example.library.pojo.Borrower;
import org.example.library.pojo.Category;

import java.util.List;

public interface BorrowService {
    List<Borrower> getBorrower(String userId);
}
