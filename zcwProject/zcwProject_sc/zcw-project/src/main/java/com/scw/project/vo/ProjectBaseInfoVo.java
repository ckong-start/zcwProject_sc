package com.scw.project.vo;

import com.scw.common.vo.BaseVo;
import com.scw.project.bean.TProjectInitiator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @create 2020-02-20 16:18
 */
@ApiModel(value = "项目发布收集的第一步信息")
@Data
public class ProjectBaseInfoVo extends BaseVo {
    @ApiModelProperty("项目之前的临时token")
    private String projectToken;// 项目的临时token

    @ApiModelProperty("项目的分类id")
    private List<Integer> typeids; // 项目的分类id

    @ApiModelProperty("项目的标签id")
    private List<Integer> tagids; // 项目的标签id

    @ApiModelProperty("项目名称")
    private String name;// 项目名称

    @ApiModelProperty("项目简介")
    private String remark;// 项目简介

    @ApiModelProperty("筹资金额")
    private Integer money;// 筹资金额

    @ApiModelProperty("筹资天数")
    private Integer day;// 筹资天数

    @ApiModelProperty("项目头部图片")
    private String headerImage;// 项目头部图片

    @ApiModelProperty("项目详情图片")
    private List<String> detailsImage;// 项目详情图片

    @ApiModelProperty("项目发布人信息")
    private TProjectInitiator projectInitiator;



}
