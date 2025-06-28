package com.assigment.authservice.service.impl;

import com.assigment.authservice.util.ConstantUtil;
import com.tracking.commonservice.dto.UserDtoRpc;
import com.tracking.commonservice.service.UserServiceRpc;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @DubboReference
    private UserServiceRpc userServiceRpc;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDtoRpc userDtoRpc = userServiceRpc.getUser(username);
        Locale locale = LocaleContextHolder.getLocale();
        if (userDtoRpc == null) {
            String message = messageSource.getMessage(ConstantUtil.CONSTRAINT_USERNAME_NOT_FOUND, new Object[]{username}, locale);
            throw new UsernameNotFoundException(message);
        }

//        return new User(
//                userDtoRpc.getUsername(),
//                userDtoRpc.getPassword(),
//                userDtoRpc.getRoles())
//        );

        return User.builder()
                .username(userDtoRpc.getUsername())
                .password(userDtoRpc.getPassword())
                .authorities(userDtoRpc.getRoles().stream().map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}
