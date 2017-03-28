package com.zhangry.demo.security.rest.token;

import java.io.Serializable;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhangry on 2017/3/28.
 */
public final class TokenInfo implements Serializable {
    private static final long serialVersionUID = -5993739014849643849L;
    private final long creationTime = System.currentTimeMillis();
    private final UserDetails userDetails;
    private final String username;
    private final String token;

    public TokenInfo(UserDetails userDetails, String username, String token) {
        this.userDetails = userDetails;
        this.username = username;
        this.token = token;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public UserDetails getUserDetails() {
        return this.userDetails;
    }

    public String getUsername() {
        return this.username;
    }

    public String getToken() {
        return this.token;
    }
}
