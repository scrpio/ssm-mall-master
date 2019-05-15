package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.Member;

public interface IMemberService extends IService<Member> {
    /**
     * 分页获得会员列表
     * @param page
     * @param limit
     * @return
     */
    ResuhSet getMemberList(int page, int limit, String condition);

    /**
     * 分页获得移除会员列表
     * @param page
     * @param limit
     * @return
     */
    ResuhSet getRemoveMemberList(int page, int limit, String condition);

    /**
     * 获得所有会员总数
     * @return
     */
    Integer getMemberCount();

    /**
     * 获得删除会员
     * @return
     */
    Integer getRemoveMemberCount();

    /**
     * 修改会员状态
     * @param id
     * @param state
     * @return
     */
    int alertMemberState(Long id, Integer state);

    /**
     * 彻底删除会员
     * @param id
     * @return
     */
    int deleteMember(Long id);

}
