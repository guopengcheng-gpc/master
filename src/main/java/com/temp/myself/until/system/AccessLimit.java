package com.temp.myself.until.system;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 限制接口访问
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    /**
     * 秒
     * @return
     */
    int seconds();

    /**
     * 最大请求数量
     * @return
     */
    int maxCount();

    /**
     * 是否需要登录
     * @return
     */
    boolean needLogin() default true;
}
