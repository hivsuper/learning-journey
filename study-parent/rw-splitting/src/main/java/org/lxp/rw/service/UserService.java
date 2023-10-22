package org.lxp.rw.service;

import java.util.Calendar;

import javax.inject.Inject;

import org.lxp.rw.mapper.UserMapper;
import org.lxp.rw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Inject
    private UserMapper userMapper;

    public User add(String name) {
        User user = new User();
        user.setName(name);
        user.setCreateTime(Calendar.getInstance().getTime());
        userMapper.insert(user);
        LOG.info("add user name={} userId={}", name, user.getId());
        return user;
    }

    public User queryByUserId(int userId) {
        return userMapper.selectById(userId);
    }

    public boolean deleteByUserId(int userId) {
        LOG.info("delete userId={}", userId);
        return userMapper.deleteById(userId) > 0;
    }

    public User updateNameByUserId(int userId, String name) {
        User user = queryByUserId(userId);
        user.setName(name);
        user.setModifyTime(Calendar.getInstance().getTime());
        userMapper.updateById(user);
        LOG.info("update userId={}'s name to {}", userId, name);
        return queryByUserId(userId);
    }
}
