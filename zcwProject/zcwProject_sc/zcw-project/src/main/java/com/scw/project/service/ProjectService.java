package com.scw.project.service;

import com.scw.project.vo.ProjectRedisStorageVo;

/**
 * @create 2020-02-20 18:54
 */

public interface ProjectService {
    //发布项目，在数据库持久保存
    void createProject(ProjectRedisStorageVo bigVo);
}
