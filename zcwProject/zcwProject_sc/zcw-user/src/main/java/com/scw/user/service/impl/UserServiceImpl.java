package com.scw.user.service.impl;

import com.scw.user.bean.TMember;
import com.scw.user.bean.TMemberExample;
import com.scw.user.exception.UserAccException;
import com.scw.user.mapper.TMemberMapper;
import com.scw.user.service.UserService;
import com.scw.user.vo.UserRegistVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @create 2020-02-11 23:11
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TMemberMapper memberMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void doRegist(UserRegistVo vo) {
//        1.验证手机号码的唯一性	自动装配TMemberMapper，查询手机号码注册的个数，如果大于0则手动抛出运行时异常
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(vo.getLoginacct());
        long l = memberMapper.countByExample(example);
        if (l > 0)throw new UserAccException("手机号码已被注册");

//        2.验证邮箱的唯一性	example.clear()，查询邮箱的注册的个数，原理同上
        example.clear();
        example.createCriteria().andEmailEqualTo(vo.getEmail());
        l = memberMapper.countByExample(example);
        if (l > 0)throw new UserAccException("邮箱已被注册");

//        3.保存注册信息到数据库中	有些字段不能为空，如果用户没有填写，需要自动给其默认值	new一个member对象，
//        使用beanUtils拷贝vo数据到member中，username和authstatus需要设置默认值，即loginacct值给username，authstatus默认为“0”
//        另外考虑到密码需要加密，使用BCryptPasswordEncoder，springboot底层默认集成了baseSecurity，调用encode方法加密密码存到member中
        TMember member = new TMember();
        BeanUtils.copyProperties(vo, member);
        member.setUsername(vo.getLoginacct());
        member.setAuthstatus("0");
        member.setUserpswd(passwordEncoder.encode(vo.getUserpswd()));
        memberMapper.insertSelective(member);
    }

    @Override
    public TMember doLogin(String loginacct, String userpswd) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(loginacct);
        List<TMember> members = memberMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(members) || members.size() > 1) throw new UserAccException("账号不存在");
        TMember member = members.get(0);
        boolean result = passwordEncoder.matches(member.getUserpswd(), userpswd);
        if (result) throw new UserAccException("密码错误");

        return member;
    }
}
