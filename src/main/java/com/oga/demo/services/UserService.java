package com.oga.demo.services;

import com.oga.demo.Entity.UserEntity;
import org.springframework.http.ResponseEntity;


import java.util.List;


public interface UserService {

    List<UserEntity> getAllUsers();

    ResponseEntity<UserEntity> getUserByID(Integer id);

    UserEntity addUser(UserEntity user);

    UserEntity updateUser(UserEntity updateuser, Integer id);

    UserEntity disableUser(Integer id);

    List<UserEntity> getAllDisabledUsers();
}
