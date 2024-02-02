package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.DTO.UserDTO;
import com.springboot.librarysystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    final private UserService userService;

    //  @Secured({"ROLE_EDITOR"})
    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.saveUser(userDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    //   @Secured({"ROLE_EDITOR","ROLE_ADMIN"})
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // userService.saveUser(new UserDTO("Robin","Hood","123654789963")); //TODO for test
        List<UserDTO> users = userService.findAllUsers();
        if (CollectionUtils.isEmpty(users)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO existUser = userService.findUserById(id);
        if (existUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(existUser, HttpStatus.OK);
        }
    }

}