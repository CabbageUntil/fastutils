package com.cabbage.controller;


import com.cabbage.bean.User;
import com.cabbage.service.UserService;
import com.core.enums.ResultCode;
import com.core.result.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/getUserAll",method = RequestMethod.GET)
    public Result getAllUser(PageInfo pageInfo) {
        return Result.success(userService.findAll(pageInfo));

    }


}
