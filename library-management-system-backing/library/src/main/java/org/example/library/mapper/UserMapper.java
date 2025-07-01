package org.example.library.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.library.pojo.User;

@Mapper
public interface UserMapper {
    @Select("select * from user where userId=#{userId}")
    User findByUserId(String userId);

    @Insert("insert into user(username,userId,password,isAdmin) values(#{username},#{userId},#{password},#{isAdmin})")
    void add(String username, String userId, String password,boolean isAdmin);
    @Update("update user set password=#{newPwd} where userId=#{userId}")
    void updatePwd(String newPwd, String userId);
}
