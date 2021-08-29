package com.joizhang.naiverpc.demo.service;

import com.joizhang.naiverpc.demo.api.dto.User;
import com.joizhang.naiverpc.demo.api.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User incrementVersion(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setGender(user.getGender());
        newUser.setAge(user.getAge());
        newUser.setVersion(user.getVersion() + 1);
        return newUser;
    }

}
