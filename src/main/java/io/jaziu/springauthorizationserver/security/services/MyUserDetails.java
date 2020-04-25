package io.jaziu.springauthorizationserver.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jaziu.springauthorizationserver.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class MyUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private boolean isEnabled;
    private Collection<? extends GrantedAuthority> authorities;



    public static MyUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new MyUserDetails(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),user.isEnabled(),authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public String getPassword() {
        return password;
    }




    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}

