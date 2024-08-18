package org.lxp.weixin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.entity.User;
import org.lxp.weixin.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> findAll(final int pageNumber, final int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }
}
