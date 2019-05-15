package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Member;
import com.mall.manager.model.Vo.ChartData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MemberMapper extends BaseMapper<Member> {
    Member findMemberByUsername(String username);

    List<Member> selectByMemberInfo(@Param("condition") String condition);

    List<Member> selectByRemoveMemberInfo(@Param("condition") String condition);

    List<ChartData> selectMemberChart(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ChartData> selectMemberByYear(@Param("year") int year);
}
