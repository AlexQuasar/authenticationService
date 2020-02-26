package com.alexquasar.authenticationService.service;

import com.alexquasar.authenticationService.dto.mailInteraction.DataMail;
import com.alexquasar.authenticationService.entity.Mail;
import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.exception.ServiceException;
import com.alexquasar.authenticationService.repository.MailRepository;
import com.alexquasar.authenticationService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthenticationService {

    private MailRepository mailRepository;
    private UserRepository userRepository;

    @Value("${authentication.delay}")
    private int delay;
    private final String key = "testKey";
    private Key securityKey;

    public AuthenticationService(MailRepository mailRepository, UserRepository userRepository) {
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;

        byte[] keys = DatatypeConverter.parseBase64Binary(key);
        securityKey = new SecretKeySpec(keys, SignatureAlgorithm.HS512.getJcaName());
    }

    public Boolean registration(DataMail dataMail) {
        Mail mail = this.mailRepository.findByLogin(dataMail.getLogin());
        if (mail == null) {
            User user = this.userRepository.findByName(dataMail.getUserName());
            if (user == null) {
                user = new User();
                user.setName(dataMail.getUserName());
            }
            this.mailRepository.save(new Mail(dataMail, user));

            return true;
        }

        return false;
    }

    public String authorization(String login, String password) {
        if (isMailExist(login, password)) {
            Date date = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
            return Jwts.builder().setId(login).setExpiration(date).signWith(SignatureAlgorithm.HS512, securityKey).compact();
        }

        return "";
    }

    public User getUser(String token) throws ServiceException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.securityKey).parseClaimsJws(token);
        Claims body = claimsJws.getBody();

        Mail mail = this.mailRepository.findByLogin(body.getId());
        if (mail == null) {
            throw new ServiceException("You not registered", HttpStatus.FORBIDDEN);
        }

        Date expiration = body.getExpiration();
        if (expiration.after(new Date(Instant.now().toEpochMilli()))) {
            User user = userRepository.findById(mail.getUser().getId());
            if (user != null) {
                return user;
            } else {
                throw new ServiceException("This user not found", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ServiceException("Please, authorized again", HttpStatus.UNAUTHORIZED);
        }
    }

    private Boolean isMailExist(String login, String password) {
        Mail mail = this.mailRepository.findByLogin(login);
        return mail != null && mail.getPassword().equals(password);
    }
}
