package com.alexquasar.authenticationService.dto.mailInteraction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataMail {

    private String login;
    private String password;
    private String userName;
}
