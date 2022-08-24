package com.example.application.service;

import com.example.application.db.entity.User;
import com.example.application.db.repository.UsersRepository;
import com.example.application.exception.UserNotFoundException;
import com.example.application.model.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl  implements  UserService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String HASH_KEY="Email";


    @Override
    @Transactional
    @CacheEvict(value="user",key="#userObj.email")
    public UserDto createUser(UserDto userDto){
        User request =new User();
        BeanUtils.copyProperties(userDto,request);
        UserDto cacheUser = isUserExistsInCache(userDto.getEmail());
        if(cacheUser==null){
            User user=this.usersRepository.saveAndFlush(request);
            BeanUtils.copyProperties(user,userDto);
            redisTemplate.opsForHash().put(HASH_KEY,userDto.getEmail(),userDto);
        }else{
            BeanUtils.copyProperties(cacheUser,userDto);
        }
        return userDto;
    }

    @Override
    @Cacheable(value="user",key="#email")
    public UserDto findUserByEmail(String email) {
        UserDto userDto = new UserDto();
        UserDto cacheUser = isUserExistsInCache(email);
        if(cacheUser == null){
            User user = this.usersRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }
        BeanUtils.copyProperties(cacheUser, userDto);
        return userDto;
    }

    private UserDto isUserExistsInCache(String email){
        UserDto cacheUser = (UserDto) redisTemplate.opsForHash().get(HASH_KEY, email);
        return cacheUser;
    }


}
