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
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@AllArgsConstructor
public class UserServiceImpl  implements UserService{


    @Autowired
   private final UserRepository userRepository;

    
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



    @SneakyThrows
    public ResponseEntity<UserEntity> getUserByID(Integer id) {
       UserEntity user =  userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("user not found"+id));
         if (user.isDisabled()==true){

             return new ResponseEntity("This Accound is disabled",HttpStatus.NOT_FOUND);
         }
         return   new ResponseEntity(user, HttpStatus.OK);
    }


    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }


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


    public UserEntity disableUser(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotAcceptableStatusException("user not found"));
        user.setDisabled(true);
        userRepository.save(user);
        return user;
    }


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


    private static String storageDirectoryPath = System.getProperty("user.dir") + "/images/";
    @Override
    public String uploadImage(MultipartFile file) {



        makeDirectoryIfNotExist(storageDirectoryPath);
        Path storageDirectory = Paths.get(storageDirectoryPath);


        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path destination = Paths.get(storageDirectory.toString() + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file

        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/images/getImage/")
                .path(fileName)
                .toUriString();
        // return the download image url as a response entity

        return fileName;
    }
    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
