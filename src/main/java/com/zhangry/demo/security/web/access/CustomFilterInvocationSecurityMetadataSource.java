package com.zhangry.demo.security.web.access;

import com.thinvent.security.core.ISecureObjectManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);
    private ISecureObjectManager secureObjectManager;
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    public CustomFilterInvocationSecurityMetadataSource() {
    }

    public synchronized void setSecureObjectManager(ISecureObjectManager secureObjectManager) {
        this.secureObjectManager = secureObjectManager;
        this.initRequestMap();
    }

    private void initRequestMap() {
        if(this.secureObjectManager == null) {
            throw new IllegalArgumentException("secureObjectManager can not be null.");
        } else {
            Map<String, Collection<ConfigAttribute>> attributes = this.secureObjectManager.loadAllConfigAttributes();
            this.requestMap = new LinkedHashMap();
            Iterator var2 = attributes.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<String, Collection<ConfigAttribute>> entry = (Entry)var2.next();
                if(!this.requestMap.containsKey(entry.getKey())) {
                    this.requestMap.put(new AntPathRequestMatcher((String)entry.getKey()), entry.getValue());
                }
            }

        }
    }

    public synchronized void refreshRequestMap() {
        long start = System.currentTimeMillis();
        Map<String, Collection<ConfigAttribute>> attributes = this.secureObjectManager.loadAllConfigAttributes();
        this.requestMap.clear();
        Iterator var4 = attributes.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<String, Collection<ConfigAttribute>> entry = (Entry)var4.next();
            if(!this.requestMap.containsKey(entry.getKey())) {
                this.requestMap.put(new AntPathRequestMatcher((String)entry.getKey()), entry.getValue());
            }
        }

        long end = System.currentTimeMillis();
        logger.debug("refreshRequestMap cost time: " + (end - start) + " mills.");
    }

    public synchronized Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation)object).getRequest();
        Iterator var3 = this.requestMap.entrySet().iterator();

        Entry entry;
        do {
            if(!var3.hasNext()) {
                return null;
            }

            entry = (Entry)var3.next();
        } while(!((RequestMatcher)entry.getKey()).matches(request));

        return (Collection)entry.getValue();
    }

    public synchronized Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet();
        Iterator var2 = this.requestMap.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<RequestMatcher, Collection<ConfigAttribute>> entry = (Entry)var2.next();
            allAttributes.addAll((Collection)entry.getValue());
        }

        return allAttributes;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}

