package com.dnd.niceteam.security;

import com.dnd.niceteam.member.domain.Member;
import com.dnd.niceteam.member.exception.MemberNotFoundException;
import com.dnd.niceteam.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findOneByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("username = " + username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().getFullName()));

        return User.withUsername(username)
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}
