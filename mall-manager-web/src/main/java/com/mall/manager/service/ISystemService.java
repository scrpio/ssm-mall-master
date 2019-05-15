package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.Base;
import com.mall.manager.model.OrderItem;
import com.mall.manager.model.SysLog;

public interface ISystemService extends IService<SysLog> {
    /**
     * 获取网站基础设置
     * @return
     */
    Base getBase();

    /**
     * 更新网站基础设置
     * @param base
     * @return
     */
    int updateBase(Base base);

    /**
     * 获取本周热销商品
     * @return
     */
    OrderItem getWeekHot();

    /**
     * 添加日志
     * @param sysLog
     * @return
     */
    int addLog(SysLog sysLog);

    /**
     * 获取日志列表
     * @param page
     * @param limit
     * @return
     */
    ResuhSet getLogList(int page, int limit);
    /**
     * 统计日志数量
     * @return
     */
    Long countLog();

    /**
     * 删除日志
     * @param id
     * @return
     */
    int deleteLog(int id);
}
