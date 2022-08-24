package com.example.application.service;

import com.example.application.model.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public UserDto createUser(UserDto userDto);
    public UserDto findUserByEmail(String email);
}
