package io.jaziu.springauthorizationserver.repository;


import io.jaziu.springauthorizationserver.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<Token,Long> {
    Token findByValue(String value);
}
