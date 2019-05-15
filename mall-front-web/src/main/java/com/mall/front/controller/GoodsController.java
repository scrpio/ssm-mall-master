package com.mall.front.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.front.service.IMatterService;
import com.mall.front.service.SearchService;
import com.mall.manager.model.Panel;
import com.mall.manager.model.PanelContent;
import com.mall.manager.model.Vo.PageResult;
import com.mall.manager.model.Vo.ProductDet;
import com.mall.manager.model.Vo.SearchResult;
import com.mall.manager.model.Vo.ZTreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "商品页面展示")
@RestController
public class GoodsController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private IMatterService matterService;
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/goods/navList", method = RequestMethod.GET)
    @ApiOperation(value = "获取导航栏")
    public Result<List<PanelContent>> getNavList() {
        List<PanelContent> list = matterService.getNavList();
        return new ResultUtil<List<PanelContent>>().setData(list);
    }

    @ApiOperation(value = "首页内容展示")
    @RequestMapping(value = "/goods/home", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<Panel>> getProductHome() {
        List<Panel> list = matterService.getHome();
        logger.info(list.toString());
        return new ResultUtil<List<Panel>>().setData(list);
    }

    @RequestMapping(value = "/goods/category", method = RequestMethod.GET)
    @ApiOperation(value = "通过父ID获取商品分类列表")
    public Result<List<ZTreeNode>> getItemCatList(@RequestParam(name = "id", defaultValue = "0") int parentId) {
        List<ZTreeNode> list = matterService.getItemCatList(parentId);
        return new ResultUtil<List<ZTreeNode>>().setData(list);
    }

    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "/goods/productDet", method = RequestMethod.GET)
    @ResponseBody
    public Result<ProductDet> getProductDet(Long productId) {
        ProductDet productDet = matterService.getProductDet(productId);
        return new ResultUtil<ProductDet>().setData(productDet);
    }

    @ApiOperation(value = "所有商品")
    @RequestMapping(value = "/goods/allGoods", method = RequestMethod.GET)
    @ResponseBody
    public Result<PageResult> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestParam(defaultValue = "") String sort,
                                            @RequestParam(defaultValue = "") Long cid,
                                            @RequestParam(defaultValue = "-1") int priceGt,
                                            @RequestParam(defaultValue = "-1") int priceLte) {
        PageResult allGoodsResult = matterService.getAllProduct(page, size, sort, cid, priceGt, priceLte);
        return new ResultUtil<PageResult>().setData(allGoodsResult);
    }

    @ApiOperation(value = "商品推荐板块")
    @RequestMapping(value = "/goods/recommend", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<Panel>> getRecommendGoods() {
        List<Panel> list = matterService.getRecommendGoods();
        return new ResultUtil<List<Panel>>().setData(list);
    }

    @RequestMapping(value = "/goods/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索商品ES")
    public Result<SearchResult> searchProduct(@RequestParam(defaultValue = "") String key,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(defaultValue = "") String sort,
                                              @RequestParam(defaultValue = "-1") int priceGt,
                                              @RequestParam(defaultValue = "-1") int priceLte) {
        SearchResult searchResult = searchService.search(key, page, size, sort, priceGt, priceLte);
        return new ResultUtil<SearchResult>().setData(searchResult);
    }

    @RequestMapping(value = "/goods/quickSearch", method = RequestMethod.GET, produces = {"text/html;charset=utf-8"})
    @ApiOperation(value = "快速搜索")
    public String getQuickSearch(@RequestParam(defaultValue = "") String key) {
        return searchService.quickSearch(key);
    }

    @RequestMapping(value = "/view/count", method = RequestMethod.GET)
    @ApiOperation(value = "统计浏览量")
    public Result<Object> getViewCount(HttpServletRequest request) {
        return new ResultUtil<Object>().setData(matterService.viewCount());
    }
}
