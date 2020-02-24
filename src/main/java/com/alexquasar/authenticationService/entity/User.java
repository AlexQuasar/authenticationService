package com.alexquasar.authenticationService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(schema = "public", name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<UserVisit> userVisits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Mail> mails;

//    @JsonIgnore
//    public Set<UserVisit> getUserVisits() {
//        return userVisits;
//    }

    @JsonIgnore
    public Set<Mail> getMails() {
        return mails;
    }
}
