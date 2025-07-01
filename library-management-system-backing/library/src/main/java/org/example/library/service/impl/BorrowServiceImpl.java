package org.example.library.service.impl;

import org.example.library.mapper.BorrowMapper;
import org.example.library.pojo.Borrower;
import org.example.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private BorrowMapper borrowMapper;


    @Override
    public List<Borrower> getBorrower(String userId) {
        return borrowMapper.findByUserId(userId);
    }
}
