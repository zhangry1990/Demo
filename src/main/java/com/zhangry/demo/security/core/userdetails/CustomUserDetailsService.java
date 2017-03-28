package com.zhangry.demo.security.core.userdetails;

import com.thinvent.security.core.IUserManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by zhangry on 2017/3/27.
 */
public class CustomUserDetailsService implements UserDetailsService {
    private IUserManager userManager;

    public CustomUserDetailsService() {
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(this.userManager == null) {
            throw new RuntimeException("Can not find userManager when loadUserByUsername.");
        } else {
            UserDetails userDetails = this.userManager.loadUserByUsername(username);
            if(userDetails == null) {
                throw new UsernameNotFoundException("Can't find " + username);
            } else {
                return userDetails;
            }
        }
    }
}
