package org.example.library.controller;

import org.example.library.pojo.Borrower;
import org.example.library.pojo.Category;
import org.example.library.pojo.Result;
import org.example.library.service.BorrowService;
import org.example.library.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @GetMapping()
    public Result<List<Borrower>>  borrowAll(@RequestHeader(name = "Authorization") String token) {
        Map<String,Object> map= JwtUtil.parseToken(token);
        String userId=map.get("userId").toString();
        List<Borrower> br=borrowService.getBorrower(userId);
        return Result.success(br);
    }
}
