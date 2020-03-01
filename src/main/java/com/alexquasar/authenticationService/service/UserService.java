package com.alexquasar.authenticationService.service;

import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void addUsers(List<User> users) {
        userRepository.saveAll(users);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
