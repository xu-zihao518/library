package org.example.library.service;

import org.example.library.pojo.User;

public interface UserService {
    User findByUserName(String username);
    User findByUserId(String userId);
    void register(String username,String userId,String password,boolean isAdmin);
    void updatePwd(String newPwd);
}
