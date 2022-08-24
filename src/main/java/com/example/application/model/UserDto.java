package com.example.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@RedisHash("userObj")
public class UserDto implements Serializable {

    @Id
    @JsonIgnore
    private Integer id;
    private String name;
    private String email;
    private Timestamp created_at;
}
