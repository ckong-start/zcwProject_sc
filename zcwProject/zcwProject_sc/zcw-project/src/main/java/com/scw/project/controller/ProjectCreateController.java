package com.scw.project.controller;

import com.scw.common.bean.AppResponse;
import com.scw.common.utils.ScwAppUtils;
import com.scw.common.vo.BaseVo;
import com.scw.project.bean.TMember;
import com.scw.project.bean.TProjectInitiator;
import com.scw.project.bean.TReturn;
import com.scw.project.service.ProjectService;
import com.scw.project.utils.OssTemplate;
import com.scw.project.vo.ProjectBaseInfoVo;
import com.scw.project.vo.ProjectConfirmVo;
import com.scw.project.vo.ProjectRedisStorageVo;
import com.scw.project.vo.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @create 2020-02-13 22:37
 */
@Controller
@Api(tags = "项目创建的模块")
@RequestMapping("/project")
public class ProjectCreateController {

    @ApiOperation("上传图片")
    @PostMapping("/create/upload")
    public AppResponse<Object> uploadImgs(MultipartFile[] files){
        //先判断files是否为空，如果为空return fail；遍历files，调用OSSTemplate上传，
        // 将返回的存储路径存入List中，作为方法的结果返回：return AppResponse.ok(list);
        if (ArrayUtils.isEmpty(files))return AppResponse.fail("10010", "没有获取需要上传的文件");

        List<String> urlList = new ArrayList<>();
        OssTemplate ossTemplate = new OssTemplate();
        for (MultipartFile file : files) {
            String fileUrl = ossTemplate.uploadFile(file);
            urlList.add(fileUrl);
        }
        // 文件上传成功返回保存图片的url地址
        return AppResponse.ok(urlList);
    }

    @Autowired
    StringRedisTemplate srt;

    @ApiOperation("项目发布准备步骤:阅读并同意协议")
    @PostMapping("/create/start")
    public AppResponse<Object> start(BaseVo vo){
//        ①验证用户是否登录：获取调用者提交的accessToken，根据token从redis中获取member对象，获取到代表已经登录
        String accessToken = vo.getAccessToken();
        if (StringUtils.isEmpty(accessToken)){
            return AppResponse.fail("10020", "请先登录");
        }
        String memberKey = "login" + accessToken + "token";
        TMember member = ScwAppUtils.redis2Obj(TMember.class, memberKey, srt);
        if (member == null){
            //用户没有登录的情况
            return AppResponse.fail("10021", "登录超时，请重新登录");
        }

//	      ②初始化BigVo对象，并初始化唯一的projecttoken键：projectToken = "project:create:temp:"+uuid
        ProjectRedisStorageVo bigVo = new ProjectRedisStorageVo();
        String uuid = UUID.randomUUID().toString().replace("_", "");
        String projectToken = "project:create:temp:"+uuid;
        bigVo.setProjectToken(projectToken);
        //设置用户的id
        bigVo.setMemberid(member.getId());

//	      ③将BigVo对象装为json字符串存到redis中
        ScwAppUtils.saveObj2Redis(bigVo, projectToken, srt);

//        ④响应projecttoken给调用者
        return AppResponse.ok(bigVo);
    }

    @ApiOperation(value = "项目发布第一步：项目及发起人信息")
    @PostMapping("/create/step1")
    public AppResponse<Object> step1(ProjectBaseInfoVo vo){
//        1.判断用户是否登录
        String memberKey = "login" + vo.getAccessToken() + "token";
        if (!srt.hasKey(memberKey)){
            //用户没有登录的情况
            return AppResponse.fail("10021", "登录超时，请重新登录");
        }

//        2.获取提交参数的projecttoken，从redis中获取bigVo对象
        ProjectRedisStorageVo bigVo = ScwAppUtils.redis2Obj(ProjectRedisStorageVo.class, vo.getProjectToken(), srt);

//        3.将本次收集到的数据设置给bigVo对象
        BeanUtils.copyProperties(vo, bigVo);

//        4.使用那个projectToken将接受了新数据的bigVo对象设置到redis中覆盖上一步的bigVo
        ScwAppUtils.saveObj2Redis(bigVo, vo.getProjectToken(), srt);

//        5.给调用者响应
        return AppResponse.ok(bigVo);
    }

    @ApiOperation(value = "项目发布第二步：收集回报信息")
    @PostMapping("/create/step2")//接受复杂类型数据：多个对象数据提交给后台，后台需要通过对象的list结合接受，并且形参使用 @RequestBody进行标注
    public AppResponse<Object> step2(@RequestBody List<ProjectReturnVo> vos){
//        1.验证登录：accessToken--参数中无法获取此参数，所以需要重新封装vo：ProjectReturnVo（也需要继承BaseVo），此时方法参数就需要改为List<ProjectReturnVo> vos
        if (CollectionUtils.isEmpty(vos)){
            return AppResponse.fail("10030", "未收集到回报信息");
        }
        String accessToken = vos.get(0).getAccessToken();
        String memberKey = "login" + accessToken + "token";
        if (!srt.hasKey(memberKey)){
            //用户没有登录的情况
            return AppResponse.fail("10021", "登录超时，请重新登录");
        }

//        2.获取redis中的bigVo
        ProjectRedisStorageVo bigVo = ScwAppUtils.redis2Obj(ProjectRedisStorageVo.class, memberKey, srt);

//        3.将回报对象的集合设置给bigVo的List<TReturn>对象，此处需要new一个新的list集合用来添加TReturn对象，再将此list集合set给bigVo
        List<TReturn> returns = new ArrayList<>();
        for (ProjectReturnVo vo : vos) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(vo, tReturn);
            returns.add(tReturn);
        }
        bigVo.setProjectReturns(returns);

//        4.将bigVo存到redis中
        ScwAppUtils.saveObj2Redis(bigVo, vos.get(0).getProjectToken(), srt);

//        5.响应数据
        return AppResponse.ok(bigVo);
    }

    @Autowired
    ProjectService projectService;

    @ApiOperation("项目发布第三步：收集确认信息") //确认后并保存到数据库
    public AppResponse<Object> step3(ProjectConfirmVo vo){
//        1.验证登录
        String memberKey = "login" + vo.getAccessToken() + "token";
        if (!srt.hasKey(memberKey)){
            //用户没有登录的情况
            return AppResponse.fail("10021", "登录超时，请重新登录");
        }

//        2.获取redis中的bigVo对象
        ProjectRedisStorageVo bigVo = ScwAppUtils.redis2Obj(ProjectRedisStorageVo.class, vo.getProjectToken(), srt);

//        3.将本次获取到的参数更新到bigVo中
        TProjectInitiator initiator = new TProjectInitiator();
        initiator.setIdcard(vo.getIdcard());
        initiator.setAccount(vo.getAccount());
        bigVo.setProjectInitiator(initiator);

//        4.将bigVo持久化，把bigVo中的数据拆分到数据库对应的表中
        if (vo.getConfirmType() == 1){
            //正式发布
            projectService.createProject(bigVo);
            srt.delete(vo.getProjectToken());
        }else{
            //存为草稿
        }
        return AppResponse.ok("发布成功");
    }
}
