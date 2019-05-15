package com.mall.manager.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "订单")
public class PayController {
//    @Autowired
//    private IOrderService orderService;
//
//    @RequestMapping(value = "/pay/pass",method = RequestMethod.GET)
//    @ApiOperation(value = "支付审核通过")
//    public Result<Object> payPass(String tokenName, String token, String id){
//
//        int result=orderService.passPay(tokenName,token,id);
//        if(result==-1){
//            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
//        }
//        if(result==0){
//            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
//        }
//        return new ResultUtil<Object>().setData("处理成功");
//    }
//
//    @RequestMapping(value = "/pay/back",method = RequestMethod.GET)
//    @ApiOperation(value = "支付审核驳回")
//    public Result<Object> backPay(String tokenName,String token,String id){
//
//        int result=orderService.backPay(tokenName,token,id);
//        if(result==-1){
//            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
//        }
//        if(result==0){
//            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
//        }
//        return new ResultUtil<Object>().setData("处理成功");
//    }
//
//    @RequestMapping(value = "/pay/passNotShow",method = RequestMethod.GET)
//    @ApiOperation(value = "支付审核通过但不展示")
//    public Result<Object> payNotShow(String tokenName,String token,String id){
//
//        int result=orderService.notShowPay(tokenName,token,id);
//        if(result==-1){
//            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
//        }
//        if(result==0){
//            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
//        }
//        return new ResultUtil<Object>().setData("处理成功");
//    }
}
