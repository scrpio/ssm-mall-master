package com.mall.manager.controller;

import com.mall.manager.service.IArticleService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Article;
import com.mall.manager.model.Vo.ResuhSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "新闻公告管理")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    @ApiOperation(value = "获得内容列表")
    public ResuhSet getArticle(@RequestParam int page, @RequestParam int limit) {
        ResuhSet result = articleService.getArticleList(page, limit);
        return result;
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过ID获取Article")
    public Article getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    @RequestMapping(value = "/article/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加文章")
    public Result<Object> addArticle(@RequestBody Article article) {
        articleService.addArticle(article);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/article/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑文章")
    public Result<Object> updateArticle(@RequestBody Article article) {
        articleService.updateArticle(article);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/article/del/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除文章")
    public Result<Object> addArticle(@PathVariable Long[] ids) {
        for (Long id : ids) {
            articleService.deleteArticle(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
