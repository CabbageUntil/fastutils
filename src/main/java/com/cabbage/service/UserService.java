package com.cabbage.service;


import com.cabbage.bean.User;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void addUser(User user);

    PageInfo findAll(PageInfo pageInfo);
}
