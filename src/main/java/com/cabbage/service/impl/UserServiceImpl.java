package com.cabbage.service.impl;

import com.cabbage.bean.User;
import com.cabbage.mapper.UserMapper;
import com.cabbage.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        logger.info("添加用户信息！");
        user.setAccount("admin");
        user.setPassword("123456");
        userMapper.insertSelective(user);
    }

    @Override
    public PageInfo findAll(PageInfo pageInfo) {

        PageHelper.startPage(1,2);
        List<User> list = userMapper.findAll();
        PageInfo<User> page = new PageInfo<User>(list);
        return page;
    }
}
