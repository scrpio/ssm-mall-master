package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.dao.ArticleMapper;
import com.mall.manager.model.Article;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResuhSet getArticleList(int page, int limit) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        EntityWrapper<Article> entityWrapper = new EntityWrapper<>();
        Wrapper<Article> wrapper = entityWrapper.orderBy("publish_time",false);
        List<Article> list = articleMapper.selectList(wrapper);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public int addArticle(Article article) {
        article.setCreated(new Date());
        return articleMapper.insert(article);
    }

    @Override
    public int updateArticle(Article article) {
        return articleMapper.updateById(article);
    }

    @Override
    public int deleteArticle(Long id) {
        return articleMapper.deleteById(id);
    }

    @Override
    public Article getArticleById(Long id) {
        return articleMapper.selectById(id);
    }
}
