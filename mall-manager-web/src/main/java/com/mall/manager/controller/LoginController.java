package com.mall.manager.controller;

import com.mall.manager.service.IUserService;
import com.mall.manager.shiro.ShiroKit;
import com.mall.common.jedis.JedisClient;
import com.mall.common.model.Captcha;
import com.mall.common.model.UserToken;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.common.util.CaptchaUtil;
import com.mall.common.util.JwtUtils;
import com.mall.common.util.NoteUtil;
import com.mall.manager.model.User;
import com.mall.manager.model.Vo.LoginUser;
import com.mall.manager.model.Vo.Router;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@Api(description = "管理员登录管理")
public class LoginController {
    @Autowired
    private JedisClient jedisClient;
    @Value("CAPTCHA")
    private String CAPTCHA;
    @Value("PARAM")
    private String PARAM;
    @Autowired
    private IUserService userService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * 初始化验证码
     *
     * @return
     */
    @RequestMapping(value = "/captcha/init", method = RequestMethod.GET)
    public Result<Object> initCaptcha() {
        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CaptchaUtil().randomStr(4);
        Captcha captcha = new Captcha();
        captcha.setCaptchaId(captchaId);
        //缓存验证码
        jedisClient.set(CAPTCHA + ":" + captchaId, code);
        log.info("captchaId:"+captchaId);
        log.info("code:"+code);
        return new ResultUtil<Object>().setData(captcha);
    }

    /**
     * 生成验证码图片
     *
     * @param captchaId
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/captcha/draw/{captchaId}", method = RequestMethod.GET)
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {
        //得到验证码 生成指定验证码
        String code = jedisClient.get(CAPTCHA + ":" + captchaId);
        CaptchaUtil vCode = new CaptchaUtil(116, 36, 4, 10, code);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    public Result<Object> login(@RequestBody LoginUser loginUser) {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        String captchaId = loginUser.getCaptchaId();
        String captcha = loginUser.getCaptcha();
        boolean remember = loginUser.isRemember();
        String token = "";

        //验证码
//        String code = jedisClient.get(CAPTCHA + ":" + captchaId);
//        if (StringUtils.isBlank(code)) {
//            return new ResultUtil<Object>().setErrorMsg("验证码已过期，请重新获取");
//        }
//
//        if (!captcha.toLowerCase().equals(code.toLowerCase())) {
//            return new ResultUtil<Object>().setErrorMsg("验证码输入错误");
//        }

        Subject subject = SecurityUtils.getSubject();
        //MD5加密
        String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
        UsernamePasswordToken authToken = new UsernamePasswordToken(username, md5Pass);
//        token.setRememberMe(remember);

        try {
            subject.login(authToken);
            User user = userService.getUserByUsername(username);
            user.setPassword(null);
            UserToken userToken = new UserToken(username, password);
            token = JwtUtils.generateToken(userToken, 2 * 60 * 60 * 1000);
            userService.clearCache(user.getId());
            Map<String, Object> result = new HashMap<>();
            result.put("token", username);
            result.put("user", user);
            result.put("router", userService.routersByUserId(user.getId()));
            result.put("perms", userService.permsByUserId(user.getId()));
            return new ResultUtil<Object>().setData(result);
        } catch (Exception e) {
            return new ResultUtil<Object>().setErrorMsg("用户名或密码错误");
        }
    }

    @RequestMapping(value = "/user/menus", method = RequestMethod.GET)
    @ApiOperation(value = "取得菜单")
    public Result<List<Router>> getMenus(){
        List<Router> result = userService.routersByUserId(ShiroKit.getUser().getId());
        return new ResultUtil<List<Router>>().setData(result);
    }
    /**
     * 注册
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    public Result<Object> register(@RequestBody LoginUser loginUser) {
        String param = jedisClient.get(PARAM + ":" + loginUser.getParamId());
        if (StringUtils.isBlank(param)) {
            return new ResultUtil<Object>().setErrorMsg("验证码已过期，请重新获取");
        }
        if (userService.getUserByUsername(loginUser.getUsername()) != null) {
            return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
        }
        if (loginUser.isSended()) {
            NoteUtil noteUtil = new NoteUtil();
            noteUtil.checkNum(loginUser.getPhone(), param);
            return new ResultUtil<Object>().setSuccessMsg("发送成功");
        } else {
            if (loginUser.getParam().equals(param)) {
                User user = new User();
                String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
                user.setPassword(md5Pass);
                user.setUsername(loginUser.getUsername());
                user.setPhone(loginUser.getPhone());
                user.setRoleId(3);
                user.setState(true);
                user.setCreated(new Date());
                this.userService.insert(user);
                return new ResultUtil<Object>().setSuccessMsg("注册成功");
            } else {
                return new ResultUtil<Object>().setErrorMsg("验证码错误");
            }
        }
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    @ApiOperation(value = "退出登录")
    public Result<Object> logout() {
        userService.clearCache(ShiroKit.getUser().getId());
        SecurityUtils.getSubject().logout();
        return new ResultUtil<Object>().setData(null);
    }

}
