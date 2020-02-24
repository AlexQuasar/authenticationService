package com.alexquasar.authenticationService.repository;

import com.alexquasar.authenticationService.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();
    User findById(int id);
    User findByName(String name);
}
