package com.alexquasar.authenticationService.web.input;

import com.alexquasar.authenticationService.dto.mailInteraction.DataMail;
//import com.alexquasar.authenticationService.entity.UserVisit;
import com.alexquasar.authenticationService.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    public String registration(@RequestBody DataMail dataMail) {
        boolean registered = this.authenticationService.registration(dataMail);
        if (registered) {
            return "you are registered!";
        } else {
            return "you are NOT registered!";
        }
    }

    @GetMapping("/authorization")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        String token = this.authenticationService.authorization(login, password);
        if (token.equals("")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sorry pal. not this time");
        } else {
            return token;
        }
    }

//    @GetMapping("/getTodayVisits/{token}")
//    public List<UserVisit> getTodayVisits(@PathVariable String token) {
//        return this.authenticationService.getTodayVisits(token);
//    }
}
