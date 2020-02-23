package com.scw.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @create 2020-02-13 20:30
 */
@Data
@ApiModel("登录成功的响应类")
public class UserRespVo {
    @ApiModelProperty("访问令牌")
    private String accessToken;
    @ApiModelProperty("用户登录的账号，即手机号码")
    private String loginacct;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("认证状态：0-未认证，1-已认证")
    private String authstatus;
    @ApiModelProperty("用户类型：")
    private String usetype;
    @ApiModelProperty("真实姓名")
    private String realname;
    @ApiModelProperty("卡号")
    private String cardnum;
    @ApiModelProperty("账号类型：0-个人，1-企业")
    private String acctype;
}
