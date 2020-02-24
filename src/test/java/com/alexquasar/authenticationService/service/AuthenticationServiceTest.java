package com.alexquasar.authenticationService.service;

import com.alexquasar.authenticationService.dto.mailInteraction.DataMail;
import com.alexquasar.authenticationService.entity.Mail;
import com.alexquasar.authenticationService.entity.User;
//import com.alexquasar.authenticationService.entity.UserVisit;
import com.alexquasar.authenticationService.repository.MailRepository;
import com.alexquasar.authenticationService.repository.UserRepository;
//import com.alexquasar.authenticationService.repository.UserVisitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    MailRepository mailRepository;

    @Mock
    UserRepository userRepository;

//    @Mock
//    UserVisitRepository userVisitRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    DataMail dataMail;
    User user;

    final String tokenHundredYears = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJtYWlsXzFAZ29vZ2xlLmNvbSIsImV4cCI6NDczNzg5MTQwNH0.3eIKjn783JMyoAROR5yjXFLJuWM3fRfVrjVTIr73REalQ1ZSfbzdjyPBN5dnBxE1qeT-vc0FJEWEap4Fvx0VMA";

    @Test
    public void registration() {
        initialSettings();

        when(userRepository.findByName(anyString())).thenReturn(user);

        assertTrue(authenticationService.registration(dataMail));
        verify(mailRepository).findByLogin(anyString());
        verify(userRepository).findByName(anyString());
        verify(mailRepository).save(any(Mail.class));
    }

    @Test
    public void authorization() {
        initialSettings();
        Mail mail = new Mail(dataMail, user);

        when(mailRepository.findByLogin(dataMail.getLogin())).thenReturn(mail);

        int delay = 5;
        assertTrue(setDelayPrivateField(delay));

        String token = authenticationService.authorization(dataMail.getLogin(), dataMail.getPassword());
        assertNotEquals("", token);
        verify(mailRepository).findByLogin(anyString());
    }

//    @Test
//    public void getTodayVisits() {
//        initialSettings();
//        Mail mail = new Mail(dataMail, user);
//
//        UserVisit userVisit = new UserVisit();
//        userVisit.setUser(user);
//        List<UserVisit> visits = Arrays.asList(userVisit);
//
//        when(mailRepository.findByLogin(anyString())).thenReturn(mail);
//        when(userRepository.findById(anyInt())).thenReturn(user);
//        when(userVisitRepository.findAllByUserAndDay(any(User.class), any(LocalDate.class))).thenReturn(visits);
//
//        List<UserVisit> userVisits = authenticationService.getTodayVisits(tokenHundredYears);
//
//        assertEquals(visits.size(), userVisits.size());
//    }

    private void initialSettings() {
        dataMail = new DataMail("test_mail@google.com", "12345", "test_user");
        user = new User();
        user.setName(dataMail.getUserName());
    }

    private Boolean setDelayPrivateField(int delay) {
        try {
            Field field = authenticationService.getClass().getDeclaredField("delay");
            field.setAccessible(true);
            field.set(authenticationService, delay);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return false;
        }
        return true;
    }
}