package com.human.graduateproject.mapper;

import com.human.graduateproject.dto.UserDto;
import com.human.graduateproject.entity.Users;

public class UserMapper {
    public static UserDto mapToUserDto(Users users){
        if (users == null) return null;
        return new UserDto(users.getId(), users.getEmail(), users.getPassword(), users.getName(), users.getRole());
    }
    public static Users mapToUser(UserDto userDto){
        if (userDto == null) return null;
        return new Users(userDto.getId(), userDto.getEmail(), userDto.getPassword(), userDto.getName(), userDto.getRole(),null);
    }
}
