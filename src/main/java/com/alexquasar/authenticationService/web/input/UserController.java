package com.alexquasar.authenticationService.web.input;

import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/userController")
@Validated
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        userService.addUser(user);
        return "user added";
    }

    @PostMapping("/addUsers")
    public String addUsers(@RequestBody List<User> users) {
        userService.addUsers(users);
        return "all users added";
    }

    @GetMapping("/findById")
    public User findById(@RequestParam int id) {
        User user = userService.findById(id);
        if (user != null) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/findByName")
    public User findByName(@RequestParam String name) {
        User user = userService.findByName(name);
        if (user != null) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }
}
