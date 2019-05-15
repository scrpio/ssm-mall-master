package com.mall.front.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Member;
import com.mall.manager.model.Vo.LoginVo;
import com.mall.manager.model.Vo.MemberVo;
import com.mall.front.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "会员注册登录")
@RestController
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/member/register", method = RequestMethod.POST)
    public Result<Object> register(@RequestBody MemberVo memberVo) {
        memberService.insert(memberVo);
        return new ResultUtil<Object>().setSuccessMsg("注册成功");
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public Result<Member> login(@RequestBody LoginVo loginVo) {
        Member member = memberService.userLogin(loginVo);
        return new ResultUtil<Member>().setData(member);
    }

    @ApiOperation(value = "判断用户是否登录")
    @RequestMapping(value = "/member/checkLogin", method = RequestMethod.GET)
    public Result<Member> getUserByToken(@RequestParam(defaultValue = "") String token) {
        Member member = memberService.getUserByToken(token);
        return new ResultUtil<Member>().setData(member);
    }

    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/member/loginOut", method = RequestMethod.GET)
    public Result<Object> logout(@RequestParam(defaultValue = "") String token) {
        memberService.logout(token);
        return new ResultUtil<Object>().setSuccessMsg("退出登录");
    }
}
