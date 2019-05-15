package com.mall.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.LoginVo;
import com.mall.manager.model.Member;
import com.mall.manager.model.Vo.MemberVo;

public interface IMemberService extends IService<Member> {
    Member findMemberByUsername(String username);

    int insert(MemberVo memberVo);

    Member userLogin(LoginVo loginVo);

    Member memberLogin(String userName,String passPwd);

    Member getUserByToken(String token);

    int logout(String token);
}
