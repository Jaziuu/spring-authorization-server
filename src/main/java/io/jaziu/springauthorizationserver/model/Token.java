package io.jaziu.springauthorizationserver.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter

public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @OneToOne
    private User user;

    public void setValue(String value) {
        this.value = value;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
