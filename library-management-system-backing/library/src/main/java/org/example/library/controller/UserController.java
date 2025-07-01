package org.example.library.controller;

import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Select;
import org.example.library.pojo.Result;
import org.example.library.pojo.User;
import org.example.library.service.UserService;
import org.example.library.utils.JwtUtil;
import org.example.library.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{2,10}$") String username, @Pattern(regexp = "^\\S{18}$") String userId, @Pattern(regexp = "^\\S{5,10}$") String password, @RequestParam(defaultValue = "false")boolean isAdmin) {
        User u=userService.findByUserId(userId);
        if(u==null) {
            userService.register(username, userId,password,isAdmin);
            return Result.success();
        }else{
            return Result.error("用户已注册");
        }
    }
    @PostMapping("/login")
    public  Result login(String userId, String password){
        User u=userService.findByUserId(userId);
        if(u==null) {
            return Result.error("不存在此用户,请注册");
        }
        if(password.equals(u.getPassword())) {
            Map<String,Object> claims=new HashMap<>();
            claims.put("userId",u.getUserId());
            claims.put("username",u.getUsername());
            String token=JwtUtil.genToken(claims);
                return Result.success(token);
        }
        return Result.error("身份证输入错误或者密码错误");
    }
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params) {
        String oldPwd=params.get("old_Pwd");
        String newPwd=params.get("new_Pwd");
        String rePwd=params.get("re_Pwd");

        if(!StringUtils.hasLength(oldPwd)|| !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }
        Map<String,Object> map=ThreadLocalUtil.get();
        String userId=map.get("userId").toString();
        User loginer=userService.findByUserId(userId);
        if(!loginer.getPassword().equals(oldPwd)){
            return Result.error("原密码错误");
        }
        if(!newPwd.equals(rePwd)) {
            return Result.error("两次填写的新密码不一样");
        }
        userService.updatePwd(newPwd);
        return Result.success();
    }
    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String,Object> map=JwtUtil.parseToken(token);
        String userId=map.get("userId").toString();
        User user=userService.findByUserId(userId);
        return Result.success(user);
    }
}
