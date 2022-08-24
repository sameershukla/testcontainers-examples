package com.example.application.service;

import com.example.application.exception.UserNotFoundException;
import com.example.application.model.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserAdminServiceImplTest {

    @Autowired
    private UserService userService;

    @Container
    public static MySQLContainer container = new MySQLContainer("mysql:latest")
            .withDatabaseName("example_db")
            .withUsername("Test")
            .withPassword("Test");

    @Container
    public static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine")).withExposedPorts(6379);


    @BeforeAll
    public static void setUp(){
        container.withReuse(true);
        container.withInitScript("src/main/resources/db.sql");
        container.start();
        redis.start();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @Test
    public void testCreateUser(){
        UserDto request = new UserDto();
        request.setEmail("Sameer@abc.com");
        request.setName("Sameer");
        request.setCreated_at(new Timestamp(System.currentTimeMillis()));
        UserDto response = userService.createUser(request);
        assertNotNull(response);
    }

    @Test
    public void testFindUserByEmailFound(){
        UserDto request = new UserDto();
        request.setEmail("Sameer@abc.com");
        request.setName("Sameer");
        request.setCreated_at(new Timestamp(System.currentTimeMillis()));
        UserDto response = userService.createUser(request);
        UserDto dto = userService.findUserByEmail("Sameer@abc.com");
        assertTrue(dto.getEmail().equals("Sameer@abc.com"));
    }

    @Test
    public void testFindUserByEmailNotFound(){
        Exception exception = assertThrows(
                          UserNotFoundException.class,
                          () -> userService.findUserByEmail("Sameer1@abc.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @AfterAll
    public static void tearDown(){
        container.stop();
        redis.stop();
    }
}
