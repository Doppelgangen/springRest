package com.controllers;

import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return allUsers;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    User getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(new User());
        return user;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    User updateUserById(@PathVariable Long id, @RequestBody User user) {
        User userToUpdate = userRepository.findById(id).orElse(new User());
        if (userToUpdate.getId() != 0L) {
            if (user.getFirstName() != null) {
                userToUpdate.setFirstName(user.getFirstName());
            }
            if (user.getNickname() != null) {
                userToUpdate.setNickname(user.getNickname());
            }
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    User deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(new User());
        if (user.getId() != 0L) {
            userRepository.delete(user);
        }
        return user;
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    User addNewUser(@RequestBody User user) {
        userRepository.save(user);
        user = userRepository.getDistinctFirstByFirstNameOrderByIdDesc(user.getFirstName());
        return user;
    }
}
