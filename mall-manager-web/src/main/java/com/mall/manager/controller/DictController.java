package com.mall.manager.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.service.IDictService;
import com.mall.common.constant.CommonConstant;
import com.mall.common.jedis.JedisClient;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Dict;
import com.mall.manager.model.Vo.ResuhSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(description = "词典库")
public class DictController {
    @Autowired
    private IDictService dictService;
    @Autowired
    private JedisClient jedisClient;

    @RequestMapping(value = "/getDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public String getDictExtList(HttpServletResponse response){
        String result = "";
        String v = jedisClient.get(CommonConstant.EXT_KEY);
        if(StringUtils.isNotBlank(v)){
            return v;
        }
        List<Dict> list=dictService.getDictList();
        for(Dict dict : list){
            result += dict.getDict() + "\n";
        }
        if(StringUtils.isNotBlank(result)) {
            jedisClient.set(CommonConstant.EXT_KEY, result);
        }
        response.addHeader(CommonConstant.LAST_MODIFIED, jedisClient.get(CommonConstant.LAST_MODIFIED));
        response.addHeader(CommonConstant.ETAG, jedisClient.get(CommonConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/getStopDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有停用词库")
    public String getStopDictList(HttpServletResponse response){
        String result = "";
        String v = jedisClient.get(CommonConstant.STOP_KEY);
        if(StringUtils.isNotBlank(v)){
            return v;
        }
        List<Dict> list=dictService.getStopList();
        for(Dict dict : list){
            result += dict.getDict() + "\n";
        }
        if(StringUtils.isNotBlank(result)){
            jedisClient.set(CommonConstant.STOP_KEY, result);
        }
        response.addHeader(CommonConstant.LAST_MODIFIED, jedisClient.get(CommonConstant.LAST_MODIFIED));
        response.addHeader(CommonConstant.ETAG, jedisClient.get(CommonConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/dict/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public ResuhSet getDictList(){
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(1, 10);
        List<Dict> list=dictService.getDictList();
        PageInfo<Dict> pageInfo = new PageInfo<>(list);
        result.setData(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        return result;
    }

    @RequestMapping(value = "/dict/stop/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有停用词库")
    public ResuhSet getStopList(){
        ResuhSet result = new ResuhSet();
        List<Dict> list=dictService.getStopList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/dict/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加词典")
    public Result<Object> addDict(@RequestBody Dict dict){
        dictService.addDict(dict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/dict/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑词典")
    public Result<Object> updateDict(@RequestBody Dict dict){
        dictService.updateDict(dict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/dict/del/{ids}",method = RequestMethod.POST)
    @ApiOperation(value = "删除词典")
    public Result<Object> delDict(@PathVariable int[] ids){
        for(int id:ids){
            dictService.delDict(id);
        }
        update();
        return new ResultUtil<Object>().setData(null);
    }

    public void update(){
        //更新词典标识
        jedisClient.set(CommonConstant.LAST_MODIFIED, String.valueOf(System.currentTimeMillis()));
        jedisClient.set(CommonConstant.ETAG, String.valueOf(System.currentTimeMillis()));
        //更新缓存
        jedisClient.del(CommonConstant.EXT_KEY);
        jedisClient.del(CommonConstant.STOP_KEY);
    }
}
