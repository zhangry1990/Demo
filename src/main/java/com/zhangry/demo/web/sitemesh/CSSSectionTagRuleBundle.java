package com.zhangry.demo.web.sitemesh;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CSSSectionTagRuleBundle implements TagRuleBundle {
    public CSSSectionTagRuleBundle() {
    }

    public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
        defaultState.addRule("css-section", new ExportTagToContentRule(siteMeshContext, (ContentProperty)contentProperty.getChild("css-section"), false));
    }

    public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
    }
}
