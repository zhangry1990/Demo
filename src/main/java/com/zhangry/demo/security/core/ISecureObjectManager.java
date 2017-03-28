package com.zhangry.demo.security.core;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.access.ConfigAttribute;

/**
 * Created by zhangry on 2017/3/27.
 */
public interface ISecureObjectManager extends ISecurityManager {
    Map<String, Collection<ConfigAttribute>> loadAllConfigAttributes();

    Collection<ConfigAttribute> loadConfigAttributes(ISecureObject var1);
}