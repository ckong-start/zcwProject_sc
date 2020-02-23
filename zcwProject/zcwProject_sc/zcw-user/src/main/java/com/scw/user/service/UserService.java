package com.scw.user.service;

import com.scw.user.bean.TMember;
import com.scw.user.vo.UserRegistVo;

/**
 * @create 2020-02-11 23:10
 */
public interface UserService {
    //注册用户
    void doRegist(UserRegistVo vo);
    //用户登录
    TMember doLogin(String loginacct, String userpswd);
}
