package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Dict;

import java.util.List;

public interface IDictService extends IService<Dict> {

    /**
     * 获取扩展词库列表
     * @return
     */
    List<Dict> getDictList();

    /**
     * 获取停用词库列表
     * @return
     */
    List<Dict> getStopList();

    /**
     * 添加
     * @param dict
     * @return
     */
    int addDict(Dict dict);

    /**
     * 更新
     * @param dict
     * @return
     */
    int updateDict(Dict dict);

    /**
     * 删除
     * @param id
     * @return
     */
    int delDict(int id);
}
