package com.scw.project.vo;

import com.scw.common.vo.BaseVo;
import com.scw.project.bean.TProjectInitiator;
import com.scw.project.bean.TReturn;
import lombok.Data;

import java.util.List;

/**
 * @create 2020-02-20 15:26
 */
@Data
public class ProjectRedisStorageVo extends BaseVo {
    private String projectToken;//项目的临时token
    private Integer memberid;//会员id
    private List<Integer> typeids; //项目的分类id
    private List<Integer> tagids; //项目的标签id
    private String name;//项目名称
    private String remark;//项目简介
    private Integer money;//筹资金额
    private Integer day;//筹资天数
    private String headerImage;//项目头部图片
    private List<String> detailsImage;//项目详情图片
    private List<TReturn> projectReturns;//项目回报
    //发起人信息：自我介绍，详细自我介绍，联系电话，客服电话
    private TProjectInitiator projectInitiator;
}
