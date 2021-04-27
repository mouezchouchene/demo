package com.oga.demo.services;

import com.oga.demo.Entity.UserEntity;
import com.oga.demo.exeption.NotFoundExeption;
import com.oga.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.Iterator;
import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{


    @Autowired
   private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

//        for (UserEntity ob : users){
//            if (ob.isStatus()==true){
//                users.remove(users.indexOf(ob));
//            }
//        }
        for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
            UserEntity user = i.next();
            if (user.isDisabled()==true){
                i.remove();
            }
        }

        return users;
    }


    @Override
    @SneakyThrows
    public ResponseEntity<UserEntity> getUserByID(Integer id) {
       UserEntity user =  userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("user not found"+id));
         if (user.isDisabled()==true){

             return new ResponseEntity("This Accound is disabled",HttpStatus.NOT_FOUND);
         }
         return   new ResponseEntity(user, HttpStatus.OK);
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity updateuser, Integer id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new NotAcceptableStatusException("user not found"));
        user.setNom(updateuser.getNom());
        user.setPrenom(updateuser.getPrenom());
        user.setTelephone(updateuser.getTelephone());
        user.setEmail(updateuser.getEmail());
        user.setPassword(updateuser.getPassword());
        userRepository.save(user);

        return user;
    }

    @Override
    public UserEntity disableUser(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotAcceptableStatusException("user not found"));
        user.setDisabled(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<UserEntity> getAllDisabledUsers() {
      List<UserEntity> users = userRepository.findAll();

      for (Iterator<UserEntity> i = users.iterator();i.hasNext();){
          UserEntity user = i.next();
          if (user.isDisabled()==false){
              i.remove();
          }
      }

         return users;
    }
}
