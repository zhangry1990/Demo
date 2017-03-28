package com.zhangry.demo.web.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {
    public CustomConfigurableSiteMeshFilter() {
    }

    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setCustomContentProcessor(new CustomContentProcessor());
    }
}
