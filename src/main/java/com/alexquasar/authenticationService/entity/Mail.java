package com.alexquasar.authenticationService.entity;

import com.alexquasar.authenticationService.dto.mailInteraction.DataMail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "mail")
@NoArgsConstructor
@Getter
@Setter
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Mail(DataMail dataMail, User user) {
        this.login = dataMail.getLogin();
        this.password = dataMail.getPassword();
        this.user = user;
    }
}
