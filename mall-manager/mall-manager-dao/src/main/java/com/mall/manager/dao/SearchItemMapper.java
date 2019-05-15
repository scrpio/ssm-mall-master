package com.mall.manager.dao;

import com.mall.manager.model.Vo.SearchItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SearchItemMapper {
    List<SearchItem> getItemList();

    SearchItem getItemById(@Param("id") Long id);
}
