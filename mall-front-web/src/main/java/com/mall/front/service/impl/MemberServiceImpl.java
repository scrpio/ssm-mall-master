package com.mall.front.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.common.result.ResultCode;
import com.mall.front.service.IMemberService;
import com.mall.manager.model.Vo.LoginVo;
import com.mall.manager.dao.MemberMapper;
import com.mall.manager.model.Member;
import com.mall.manager.model.Vo.MemberVo;
import com.mall.manager.model.factory.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    @Autowired
    private MemberMapper memberDao;
    @Autowired
    private JedisClient jedisClient;

    public Member findMemberByUsername(String username) {
        return memberDao.findMemberByUsername(username);
    }

    @Override
    public int insert(MemberVo memberVo) {
        if (memberDao.findMemberByUsername(memberVo.getUserName()) != null) {
            throw new StoreException(ResultCode.TAKES);
        }
        String password = DigestUtils.md5DigestAsHex(memberVo.getUserPwd().getBytes());
        memberVo.setUserPwd(password);
        memberVo.setState(1);
        memberVo.setCreated(new Date());
        memberVo.setUpdated(new Date());
        memberDao.insert(ModelFactory.MemberVoToMember(memberVo));
        return 1;
    }

    @Override
    public Member userLogin(LoginVo memberVo) {
        Member member = memberDao.findMemberByUsername(memberVo.getUsername());
        if (member == null) {
            Member tbMember = new Member();
            tbMember.setState(0);
            tbMember.setMessage("用户名或密码错误");
            return tbMember;
        }
        //md5加密
        if (!DigestUtils.md5DigestAsHex(memberVo.getPassword().getBytes()).equals(member.getPassword())) {
            Member tbMember = new Member();
            tbMember.setState(0);
            tbMember.setMessage("用户名或密码错误");
            return tbMember;
        }
        String token = UUID.randomUUID().toString();
        member.setToken(token);
        member.setState(1);
        // 用户信息写入redis：key："SESSION:token" value："user"
        jedisClient.set("SESSION:" + token, new Gson().toJson(member));
        jedisClient.expire("SESSION:" + token, 1800);
        return member;
    }

    @Override
    public Member memberLogin(String userName, String passPwd) {
        Member member = memberDao.findMemberByUsername(userName);
        if (member == null) {
            Member tbMember = new Member();
            tbMember.setState(0);
            tbMember.setMessage("用户名错误");
            return tbMember;
        }
        //md5加密
        if (!DigestUtils.md5DigestAsHex(passPwd.getBytes()).equals(member.getPassword())) {
            Member tbMember = new Member();
            tbMember.setState(0);
            tbMember.setMessage("用户名或密码错误");
            return tbMember;
        }
        String token = UUID.randomUUID().toString();
        member.setToken(token);
        member.setState(1);
        // 用户信息写入redis：key："SESSION:token" value："user"
        jedisClient.set("SESSION:" + token, new Gson().toJson(member));
        jedisClient.expire("SESSION:" + token, 1800);
        return member;
    }

    @Override
    public Member getUserByToken(String token) {
        String json = jedisClient.get("SESSION:" + token);
        if (json == null) {
            Member member = new Member();
            member.setState(0);
            member.setMessage("用户登录已过期");
            return member;
//            throw new StoreException(ResultCode.OVERTIME);
        }
        //重置过期时间
        jedisClient.expire("SESSION:" + token, 1800);
        Member member = new Gson().fromJson(json, Member.class);
        return member;
    }

    @Override
    public int logout(String token) {
        jedisClient.del("SESSION:" + token);
        return 1;
    }
}
