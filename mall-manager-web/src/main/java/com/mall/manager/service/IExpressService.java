package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Express;
import com.mall.manager.model.Vo.ResuhSet;

public interface IExpressService extends IService<Express> {
    /**
     * 获取快递列表
     * @return
     */
    ResuhSet getExpressList(int page, int limit);

    /**
     * 添加快递
     * @param express
     * @return
     */
    int addExpress(Express express);

    /**
     * 更新快递
     * @param express
     * @return
     */
    int updateExpress(Express express);

    /**
     * 删除快递
     * @param id
     * @return
     */
    int delExpress(int id);
}
