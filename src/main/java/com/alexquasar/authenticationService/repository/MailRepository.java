package com.alexquasar.authenticationService.repository;

import com.alexquasar.authenticationService.entity.Mail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MailRepository extends CrudRepository<Mail, Long> {

    List<Mail> findAll();
    Mail findByLogin(String login);
}
