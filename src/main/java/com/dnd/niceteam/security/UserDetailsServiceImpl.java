package com.dnd.niceteam.security;

import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class       : UserDetailsImpl
 * Author      : 문 윤지
 * Description : UserDetailsService 구현체
 * History     : [2022-07-15] 문윤지 - Class Create
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username = " + username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole().getFullName()));

        return User.withUsername(username)
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}
