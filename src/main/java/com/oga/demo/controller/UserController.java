package com.oga.demo.controller;

import com.oga.demo.Entity.UserEntity;
import com.oga.demo.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@AllArgsConstructor
public class UserController {


@Autowired
private UserService userService;

@GetMapping("users")
public List<UserEntity> getAllUsers(){

    return userService.getAllUsers();
   }
    @GetMapping("disabledusers")
    public List<UserEntity> getAllDisabledUser(){
    return userService.getAllDisabledUsers();
    }


   @GetMapping("user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable(value = "id") int id){
    return userService.getUserByID(id);

   }

   @PostMapping("users")
    public UserEntity addUser(@RequestBody UserEntity user){
    return userService.addUser(user);
   }

@PutMapping("user/{id}")
    public UserEntity updateUser(@PathVariable(value = "id") int id, @RequestBody UserEntity user){
    return  userService.updateUser(user,id);

}

@DeleteMapping("user/{id}")
    public UserEntity disableUser(@PathVariable(value = "id") int id){

    return userService.disableUser(id);
}



}