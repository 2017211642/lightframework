package org.framework.core;

/**
 * 第三方比如 mybatis 通过实现这个接口 来添加bean到 ioc容器中
 *
 * */
public interface OtherBeanRegistor {
    public void registor();
}
