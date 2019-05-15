package com.mall.manager.service;

import com.mall.manager.model.Vo.EsInfo;

public interface SearchItemService {
    /**
     * 同步索引
     * @return
     */
    int importAllItems();

    /**
     * 获取ES基本信息
     * @return
     */
    EsInfo getEsInfo();
}
