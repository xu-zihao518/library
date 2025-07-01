package org.example.library.service.impl;

import org.example.library.mapper.UserMapper;
import org.example.library.pojo.User;
import org.example.library.service.UserService;
import org.example.library.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        return null;
    }

    @Override
    public User findByUserId(String userId) {
        User u=userMapper.findByUserId(userId);
        return u;

    }

    @Override
    public void register(String username,String userId,String password,boolean isAdmin) {
    userMapper.add(username,userId,password,isAdmin);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();
        String userId=map.get("userId").toString();
        userMapper.updatePwd(newPwd,userId);
    }
}
