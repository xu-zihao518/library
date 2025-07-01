package org.example.library.pojo;

import lombok.Data;

@Data
public class User {
    private String username;
    private String userId;
    private String password;
    private int isAdmin;//1表示是管理员，0表示是学生

}
