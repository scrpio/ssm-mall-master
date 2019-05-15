package com.mall.manager.Aspect;

import com.mall.manager.shiro.ShiroKit;
import com.mall.common.util.IPInfoUtil;
import com.mall.common.util.ThreadPoolUtil;
import com.mall.manager.model.SysLog;
import com.mall.manager.service.ISystemService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class SystemLogAspect {
    private Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal beginTime");

    @Autowired
    private ISystemService systemService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(com.mall.common.annotattion.SystemServiceLog)")
    public void serviceAspect() {
        logger.info("========serviceAspect===========");
    }

    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void controllerAspect() {
        logger.info("========controllerAspect===========");
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            description = method.getAnnotation(ApiOperation.class).value();
        }
        return description;
    }

    /**
     * 保存日志
     */
    private static class SaveSystemLogThread implements Runnable {
        private SysLog sysLog;
        private ISystemService systemService;

        public SaveSystemLogThread(SysLog sysLog, ISystemService systemService) {
            this.sysLog = sysLog;
            this.systemService = systemService;
        }

        @Override
        public void run() {
            systemService.addLog(sysLog);
        }
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }

    /**
     * 后置通知(在方法执行之后返回) 用于拦截Controller层操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            String username = ShiroKit.getUser().getUsername();
            if (null != username && !request.getMethod().equals("GET")) {
                SysLog sysLog = new SysLog();
                sysLog.setName(getControllerMethodDescription(joinPoint));
                sysLog.setType(1);
                sysLog.setUrl(request.getRequestURI());
                sysLog.setRequestType(request.getMethod());
                Map<String, String[]> logParams = request.getParameterMap();
                sysLog.setMapToParams(logParams);
                sysLog.setUser(username);
                sysLog.setIp(IPInfoUtil.getIpAddr(request));
                sysLog.setIpInfo(IPInfoUtil.getIpCity(IPInfoUtil.getIpAddr(request)));
                Date logStartTime = beginTimeThreadLocal.get();
                long beginTime = beginTimeThreadLocal.get().getTime();
                long endTime = System.currentTimeMillis();
                Long logElapsedTime = endTime - beginTime;
                sysLog.setTime(Math.toIntExact(logElapsedTime));
                sysLog.setCreateDate(logStartTime);
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(sysLog, systemService));
            }
        } catch (Exception e) {
            logger.error("Controller层AOP后置通知异常", e);
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            String username = ShiroKit.getUser().getUsername();
            if (null != username) {
                SysLog sysLog = new SysLog();
                //日志标题
                sysLog.setName(getControllerMethodDescription(joinPoint));
                //日志类型
                sysLog.setType(0);
                //日志请求url
                sysLog.setUrl(request.getRequestURI());
                //请求方式
                sysLog.setRequestType(request.getMethod());
                //请求参数
                Map<String, String[]> logParams = request.getParameterMap();
                sysLog.setMapToParams(logParams);
                //请求用户
                sysLog.setUser(username);
                //请求IP
                sysLog.setIp(IPInfoUtil.getIpAddr(request));
                //IP地址
                sysLog.setIpInfo(IPInfoUtil.getIpCity(IPInfoUtil.getIpAddr(request)));
                //请求开始时间
                Date logStartTime = beginTimeThreadLocal.get();
                long beginTime = beginTimeThreadLocal.get().getTime();
                long endTime = System.currentTimeMillis();
                //请求耗时
                Long logElapsedTime = endTime - beginTime;
                sysLog.setTime(Math.toIntExact(logElapsedTime));
                sysLog.setCreateDate(logStartTime);
                //调用线程保存至数据库
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(sysLog, systemService));
            }
        } catch (Exception e1) {
            logger.error("service层AOP异常通知异常", e1);
        }
    }
}
