package com.mall.front.limit;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

@Component
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {
//    @Autowired
//    private LimitRaterInterceptor limitRaterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        registry.addInterceptor(new LimitRaterInterceptor(100, LimitRaterInterceptor.LimitType.DROP)).addPathPatterns("/**");
        //限流可配置为LimitRaterInterceptor.LimitType.DROP丢弃请求或者LimitRaterInterceptor.LimitType.WAIT等待，100为每秒的速率
        super.addInterceptors(registry);
    }
}
