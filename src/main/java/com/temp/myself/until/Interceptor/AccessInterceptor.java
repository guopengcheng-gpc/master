package com.temp.myself.until.Interceptor;

import com.alibaba.fastjson.JSON;
import com.temp.myself.until.system.AccessLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AccessInterceptor  extends HandlerInterceptorAdapter  {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*@Autowired
    private RedisUtil redisUtil;*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断请求是否属于方法的请求
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }

            // 单位时间
            int seconds = accessLimit.seconds();
            // 访问次数
            int maxCount = accessLimit.maxCount();
            // 是否需要登陆
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            logger.info("访问地址: {}", key);
            // 是否需要登陆
            if(needLogin) {
                String username = request.getHeader("username");
                key = key + "-" + username;
            }

            // 已访问次数
            /*Object o = redisUtil.get(key);
            if(Objects.isNull(o)) {
                // 第一次访问
                redisUtil.incr(key, 1);
                redisUtil.expire(key, seconds);
            } else {
                // 获取单位时间内已访问次数
                Integer count = Integer.valueOf(redisUtil.get(key).toString());
                if(maxCount > count) {
                    // 没超出访问限制
                    redisUtil.incr(key, 1);
                } else {
                    // 超出访问限制
                    logger.info("访问次数超出限制");
                    Map failure = new HashMap<>();
                    failure.put(200, "访问次数超出限制");
                    render(response, failure);
                    return false;
                }
            }*/
        }
        return true;
    }


    /**
     * 封装消息
     * @param response
     * @param message
     * @throws Exception
     */
    private void render(HttpServletResponse response, Map<String, Object> message)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(message);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
