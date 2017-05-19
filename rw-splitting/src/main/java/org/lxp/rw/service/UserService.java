package org.lxp.rw.service;

import java.util.Calendar;

import javax.annotation.Resource;

import org.lxp.rw.mapper.UserMapper;
import org.lxp.rw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    public User add(String name) {
        User user = new User();
        user.setName(name);
        user.setCreateTime(Calendar.getInstance().getTime());
        userMapper.insert(user);
        LOG.info("add user name={} userId={}", name, user.getId());
        return user;
    }

    public User queryUserById(int userId) {
        return userMapper.selectById(userId);
    }

    public boolean deleteUserById(int userId) {
        LOG.info("delete userId={}", userId);
        return userMapper.deleteById(userId) > 0;
    }
}
