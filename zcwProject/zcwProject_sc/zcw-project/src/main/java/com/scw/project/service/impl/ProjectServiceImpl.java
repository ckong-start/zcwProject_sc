package com.scw.project.service.impl;

import com.scw.common.utils.ScwAppUtils;
import com.scw.project.bean.*;
import com.scw.project.mapper.*;
import com.scw.project.service.ProjectService;
import com.scw.project.vo.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @create 2020-02-20 18:55
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    TProjectMapper projectMapper;
    @Autowired
    TProjectImagesMapper projectImagesMapper;
    @Autowired
    TProjectInitiatorMapper projectInitiatorMapper;
    @Autowired
    TProjectTagMapper projectTagMapper;
    @Autowired
    TProjectTypeMapper projectTypeMapper;
    @Autowired
    TReturnMapper returnMapper;

    @Override
    public void createProject(ProjectRedisStorageVo bigVo) {
        //1.project表保存
        TProject project = new TProject();
        BeanUtils.copyProperties(bigVo, project);
        //两者类型不一致
        project.setMoney((long)bigVo.getMoney());
        //手动设置默认值
        project.setStatus("0");
        project.setCreatedate(ScwAppUtils.getFormatTime());
        projectMapper.insertSelective(project);

        //2.project-images表的保存
        //先保存头图
        TProjectImages headImg = new TProjectImages();
        List<TProjectImages> imgs =  new ArrayList<>();
        headImg.setProjectid(project.getId());
        headImg.setImgurl(bigVo.getHeaderImage());
        headImg.setImgtype((byte) 0);
        imgs.add(headImg);
        //再保存详情图
        for (String url : bigVo.getDetailsImage()) {
            TProjectImages detailImg = new TProjectImages();
            detailImg.setProjectid(project.getId());
            detailImg.setImgurl(url);
            detailImg.setImgtype((byte) 1);
            imgs.add(detailImg);
        }
        projectImagesMapper.batchInsertImg(imgs);

        //3.projectInitiator的保存
        TProjectInitiator initiator = bigVo.getProjectInitiator();
        initiator.setProjectid(project.getId());
        projectInitiatorMapper.insertSelective(initiator);

        //4.project_tag和project_type的保存
        ArrayList<TProjectTag> tags = new ArrayList<>();
        List<Integer> tagids = bigVo.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tag = new TProjectTag();
            tag.setProjectid(project.getId());
            tag.setTagid(tagid);
            tags.add(tag);
        }
        projectTagMapper.batchInsertTags(tags);
        ArrayList<TProjectType> types = new ArrayList<>();
        List<Integer> typeids = bigVo.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType type = new TProjectType();
            type.setProjectid(project.getId());
            type.setTypeid(typeid);
            types.add(type);
        }
        projectTypeMapper.batchInsertType(types);

        //5.return的保存
        List<TReturn> tReturns = bigVo.getProjectReturns();
        for (TReturn tReturn : tReturns) {
            tReturn.setProjectid(project.getId());
        }
        returnMapper.batchInsertReturn(tReturns);
    }
}
