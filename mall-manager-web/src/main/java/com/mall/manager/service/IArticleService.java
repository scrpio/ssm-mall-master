package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Article;
import com.mall.manager.model.Vo.ResuhSet;

public interface IArticleService extends IService<Article> {
    /**
     * 获取列表
     * @return
     */
    ResuhSet getArticleList(int page, int limit);
    int addArticle(Article article);
    int updateArticle(Article article);
    int deleteArticle(Long id);
    Article getArticleById(Long id);
}
