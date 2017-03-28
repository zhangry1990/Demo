package com.zhangry.demo.security.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

/**
 * Created by zhangry on 2017/3/27.
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {
    private List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList();
    private boolean allowIfAllAbstainDecisions = false;

    public CustomAccessDecisionManager() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        this.decisionVoters.add(roleVoter);
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        this.decisionVoters.add(authenticatedVoter);
    }

    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        int deny = 0;
        Iterator var5 = this.decisionVoters.iterator();

        while(var5.hasNext()) {
            AccessDecisionVoter voter = (AccessDecisionVoter)var5.next();
            int result = voter.vote(authentication, object, configAttributes);
            switch(result) {
                case -1:
                    ++deny;
                    break;
                case 1:
                    return;
            }
        }

        if(deny > 0) {
            throw new AccessDeniedException("Access is denied");
        } else {
            this.checkAllowIfAllAbstainDecisions();
        }
    }

    public boolean supports(ConfigAttribute attribute) {
        Iterator var2 = this.decisionVoters.iterator();

        AccessDecisionVoter voter;
        do {
            if(!var2.hasNext()) {
                return false;
            }

            voter = (AccessDecisionVoter)var2.next();
        } while(!voter.supports(attribute));

        return true;
    }

    public boolean supports(Class<?> clazz) {
        Iterator var2 = this.decisionVoters.iterator();

        AccessDecisionVoter voter;
        do {
            if(!var2.hasNext()) {
                return true;
            }

            voter = (AccessDecisionVoter)var2.next();
        } while(voter.supports(clazz));

        return false;
    }

    protected final void checkAllowIfAllAbstainDecisions() {
        if(!this.isAllowIfAllAbstainDecisions()) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    public boolean isAllowIfAllAbstainDecisions() {
        return this.allowIfAllAbstainDecisions;
    }

    public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
        this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
    }
}

