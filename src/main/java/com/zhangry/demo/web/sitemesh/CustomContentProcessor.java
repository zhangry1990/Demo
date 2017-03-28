package com.zhangry.demo.web.sitemesh;

import java.io.IOException;
import java.nio.CharBuffer;
import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.tagrules.TagBasedContentProcessor;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.decorate.DecoratorTagRuleBundle;
import org.sitemesh.content.tagrules.html.CoreHtmlTagRuleBundle;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;
import org.sitemesh.webapp.WebAppContext;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomContentProcessor extends TagBasedContentProcessor {
    public CustomContentProcessor() {
        this(new TagRuleBundle[]{new CoreHtmlTagRuleBundle(), new DecoratorTagRuleBundle(), new ExSectionTagRuleBundle(), new DivExtractingTagRuleBundle()});
    }

    public CustomContentProcessor(TagRuleBundle... tagRuleBundles) {
        super(tagRuleBundles);
    }

    public Content build(CharBuffer data, SiteMeshContext siteMeshContext) throws IOException {
        WebAppContext appContext = (WebAppContext)siteMeshContext;
        return appContext.getRequest().getAttribute("exception") != null?null:(appContext.getRequest().getHeader("X-Requested-With") != null && appContext.getRequest().getHeader("X-Requested-With").equals("XMLHttpRequest")?null:super.build(data, siteMeshContext));
    }
}