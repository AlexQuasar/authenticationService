package com.alexquasar.authenticationService.service;

import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setName("UserName");
    }

    @Test
    public void addUser() {
        userService.addUser(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void addUsers() {
        List<User> users = Collections.singletonList(user);

        userService.addUsers(users);
        verify(userRepository).saveAll(anyList());
    }

    @Test
    public void findById() {
        when(userRepository.findById(anyInt())).thenReturn(user);

        User findUser = userService.findById(user.getId());
        assertEquals(user, findUser);
    }

    @Test
    public void findByName() {
        when(userRepository.findByName(anyString())).thenReturn(user);

        User findUser = userService.findByName(user.getName());
        assertEquals(user, findUser);
    }

    @Test
    public void findAllTest() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.findAll();

        assertEquals(users, userList);
    }
}