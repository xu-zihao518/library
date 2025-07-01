package org.example.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.library.pojo.Borrower;


import java.util.List;

@Mapper
public interface BorrowMapper {
    @Select("Select * from borrow where userId=#{userId}")
    List<Borrower> findByUserId(String userId);
}
