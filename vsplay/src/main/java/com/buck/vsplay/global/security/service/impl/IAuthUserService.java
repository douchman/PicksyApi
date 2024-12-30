package com.buck.vsplay.global.security.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthUserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
