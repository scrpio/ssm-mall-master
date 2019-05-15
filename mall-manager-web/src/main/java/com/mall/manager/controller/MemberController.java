package com.mall.manager.controller;

import com.mall.manager.service.IMemberService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Member;
import com.mall.manager.model.Vo.MemberVo;
import com.mall.manager.model.Vo.ResuhSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "会员管理")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @RequestMapping(value = "/member/list", method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索获取会员列表")
    public ResuhSet getMemberList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        ResuhSet result = memberService.getMemberList(page, limit, condition);
        return result;
    }

    @RequestMapping(value = "/member/list/remove", method = RequestMethod.GET)
    @ApiOperation(value = "分页多条件搜索已删除会员列表")
    public ResuhSet getDelMemberList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        ResuhSet result = memberService.getRemoveMemberList(page, limit, condition);
        return result;
    }

    @RequestMapping(value = "/member/count", method = RequestMethod.GET)
    @ApiOperation(value = "获得总会员数目")
    public Result<Object> getMemberCount() {
        Integer result = memberService.getMemberCount();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/count/remove", method = RequestMethod.GET)
    @ApiOperation(value = "获得移除总会员数目")
    public Result<Object> getRemoveMemberCount() {
        Integer result = memberService.getRemoveMemberCount();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/state", method = RequestMethod.POST)
    @ApiOperation(value = "修改会员状态")
    public Result<Member> startMember(@RequestBody MemberVo memberVo) {
        memberService.alertMemberState(memberVo.getId(), memberVo.getState());
        return new ResultUtil<Member>().setData(null);
    }

    @RequestMapping(value = "/member/recover/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "还原会员")
    public Result<Member> recoverMember(@PathVariable Long[] ids) {
        for (Long id : ids) {
            memberService.alertMemberState(id, 1);
        }
        return new ResultUtil<Member>().setData(null);
    }

    @RequestMapping(value = "/member/remove/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "移除会员")
    public Result<Member> removeMember(@PathVariable Long[] ids) {
        for (Long id : ids) {
            memberService.alertMemberState(id, 2);
        }
        return new ResultUtil<Member>().setData(null);
    }

    @RequestMapping(value = "/member/delete/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "彻底删除会员")
    public Result<Member> deleteMember(@PathVariable Long[] ids) {
        for (Long id : ids) {
            memberService.deleteMember(id);
        }
        return new ResultUtil<Member>().setData(null);
    }
}
