package com.scw.project.mapper;

import com.scw.project.bean.TProjectImages;
import com.scw.project.bean.TProjectImagesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TProjectImagesMapper {
    long countByExample(TProjectImagesExample example);

    int deleteByExample(TProjectImagesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TProjectImages record);

    int insertSelective(TProjectImages record);

    List<TProjectImages> selectByExample(TProjectImagesExample example);

    TProjectImages selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TProjectImages record, @Param("example") TProjectImagesExample example);

    int updateByExample(@Param("record") TProjectImages record, @Param("example") TProjectImagesExample example);

    int updateByPrimaryKeySelective(TProjectImages record);

    int updateByPrimaryKey(TProjectImages record);
    //批量插入图片
    void batchInsertImg(@Param("imgs") List<TProjectImages> imgs);
}