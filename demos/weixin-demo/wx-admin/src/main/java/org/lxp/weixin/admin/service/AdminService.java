package org.lxp.weixin.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.admin.exception.AdminException;
import org.lxp.weixin.admin.repository.AdminRepository;
import org.lxp.weixin.entity.Admin;
import org.lxp.weixin.exception.ErrorCodeEnum;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var adminOptional = adminRepository.findOne(Example.of(Admin.builder().username(username).build()));
        if (adminOptional.isEmpty()) {
            throw new AdminException(ErrorCodeEnum.ADMIN_NOT_FOUND);
        }
        final var admin = adminOptional.get();
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority(admin.getRole().name()))
        );
    }
}
