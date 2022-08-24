package com.example.application.service;

import com.example.application.model.UserDto;

import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    //@Autowired
    private UserService userService;


    //@Test
    //@Transactional
    public void testCreateUser(){
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setName("Test");
        userDto.setCreated_at(new Timestamp(System.currentTimeMillis()));
        this.userService.createUser(userDto);
        UserDto user = this.userService.findUserByEmail("test@test.com");
        assertNotNull(user);
        assertTrue(user.getName().equals("Test"));
    }
}
