package org.lxp.weixin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.entity.Admin;
import org.lxp.weixin.exception.ErrorCodeEnum;
import org.lxp.weixin.exception.WXException;
import org.lxp.weixin.repository.AdminRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void login(final String username, final String password) {
        final var rtn = adminRepository.findOne(Example.of(Admin.builder().username(username).build()));
        if (rtn.isEmpty()) {
            throw new WXException(ErrorCodeEnum.ADMIN_NOT_FOUND);
        }
        final var admin = rtn.get();
        if (!Objects.equals(password, admin.getPassword())) {
            throw new WXException(ErrorCodeEnum.PASSWORD_MISMATCHED);
        }
    }
}
