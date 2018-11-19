package com.jianguo.userthriftservice.service;

import com.jianguo.thrift.user.TeacherDTO;
import com.jianguo.thrift.user.UserDTO;
import com.jianguo.thrift.user.UserService;
import com.jianguo.userthriftservice.domain.User;
import com.jianguo.userthriftservice.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService.Iface {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getUserById(int id) {
        User user = userMapper.getUserById(id);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserByName(String username) {
        User user = userMapper.getUserByName(username);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userMapper.registerUser(user);
    }

    @Override
    public TeacherDTO getTeacherById(int id) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userMapper.getTeacherById(id), teacherDTO);
        return teacherDTO;
    }
}
