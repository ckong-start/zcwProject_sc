package com.scw.project.vo;

import com.scw.common.vo.BaseVo;
import lombok.Data;

/**
 * @create 2020-02-20 18:14
 */
@Data
public class ProjectConfirmVo extends BaseVo {
    //confirmType（项目提交的方式：0--草稿，1--发布）、projectToken、account（收款账号)、idcard（法人身份证号码)
    private Integer confirmType;
    private String projectToken;
    private String account;
    private String idcard;
}
