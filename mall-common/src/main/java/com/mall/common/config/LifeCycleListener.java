package com.mall.common.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.DriverManager;

import com.mall.common.util.ThreadPoolUtil;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class LifeCycleListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //容器启动时执行的代码
        ServletContext context = servletContextEvent.getServletContext();
        String rootPath = context.getRealPath("/");
        System.setProperty("rootPath", rootPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //容器关闭时时执行的代码
        try{
            //安全删除注册的mysql驱动
            while(DriverManager.getDrivers().hasMoreElements()){
                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
            }
            AbandonedConnectionCleanupThread.shutdown();
            ThreadPoolUtil.getPool().shutdown();//关闭tomcat前关闭线程
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
