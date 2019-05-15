package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.service.IMemberService;
import com.mall.common.exception.StoreException;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.common.result.ResultCode;
import com.mall.manager.dao.MemberMapper;
import com.mall.manager.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper,Member> implements IMemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public ResuhSet getMemberList(int page, int limit, String condition) {
        ResuhSet result = new ResuhSet();
        try {
            PageHelper.startPage(page, limit);
            List<Member> list = memberMapper.selectByMemberInfo(condition);
            PageInfo<Member> pageInfo = new PageInfo<>(list);
            for (Member member : list) {
                member.setPassword("");
            }
            result.setRecordsTotal((int) pageInfo.getTotal());
            result.setSuccess(true);
            result.setData(list);
        } catch (Exception e) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public ResuhSet getRemoveMemberList(int page, int limit, String condition) {
        ResuhSet result = new ResuhSet();
        try {
            PageHelper.startPage(page, limit);
            List<Member> list = memberMapper.selectByRemoveMemberInfo(condition);
            PageInfo<Member> pageInfo = new PageInfo<>(list);
            for (Member member : list) {
                member.setPassword("");
            }
            result.setRecordsTotal((int) pageInfo.getTotal());
            result.setSuccess(true);
            result.setData(list);
        } catch (Exception e) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public Integer getMemberCount() {
        EntityWrapper<Member> entityWrapper = new EntityWrapper<>();
        Wrapper<Member> wrapper = entityWrapper.ne("state", 2);
        Integer result = memberMapper.selectCount(wrapper);
        if (result == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public Integer getRemoveMemberCount() {
        EntityWrapper<Member> entityWrapper = new EntityWrapper<>();
        Wrapper<Member> wrapper = entityWrapper.eq("state", 2);
        Integer result = memberMapper.selectCount(wrapper);
        if (result == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public int alertMemberState(Long id, Integer state) {
        Member member = memberMapper.selectById(id);
        member.setState(state);
        member.setUpdated(new Date());
        if (memberMapper.updateById(member) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public int deleteMember(Long id) {
        if (memberMapper.deleteById(id) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 0;
    }

}
