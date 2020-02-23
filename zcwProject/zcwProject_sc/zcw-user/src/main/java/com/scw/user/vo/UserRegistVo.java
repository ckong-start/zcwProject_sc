package com.scw.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @create 2020-02-11 22:34
 */
@Data
@ApiModel("用户注册的vo类")
public class UserRegistVo {
    //用户注册的VO类：UserRegistVO，有5个字段：loginacct（注册的手机号码）、
    // userpswd、email、usertype（用户类型：0-个人、1-企业）、code（注册验证码，数据库没有对应属性）
    @ApiModelProperty("注册时的手机号码，即用户账号")
    private String loginacct;
    @ApiModelProperty("用户密码")
    private String userpswd;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户类型：0-个人、1-企业")
    private String usertype;
    @ApiModelProperty("注册验证码")
    private String code;
}
