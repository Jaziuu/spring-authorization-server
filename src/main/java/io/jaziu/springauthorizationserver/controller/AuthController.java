package io.jaziu.springauthorizationserver.controller;


import io.jaziu.springauthorizationserver.message.request.LoginForm;
import io.jaziu.springauthorizationserver.message.request.SignUpForm;
import io.jaziu.springauthorizationserver.message.response.JwtResponse;
import io.jaziu.springauthorizationserver.message.response.ResponseMessage;
import io.jaziu.springauthorizationserver.model.Token;
import io.jaziu.springauthorizationserver.model.User;
import io.jaziu.springauthorizationserver.repository.TokenRepository;
import io.jaziu.springauthorizationserver.repository.UserRepository;
import io.jaziu.springauthorizationserver.security.jwt.JwtProvider;
import io.jaziu.springauthorizationserver.security.services.MyUserDetails;
import io.jaziu.springauthorizationserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private UserService userService;
    private TokenRepository tokenRepository;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtProvider, UserService userService, TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;

        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    JwtProvider jwtProvider;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User( signUpRequest.getUsername(),
                signUpRequest.getEmail(),signUpRequest.getPassword());

        userService.addUser(user);


        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @GetMapping("/token")
    public String singup(@RequestParam String value) {
        Token byValue = tokenRepository.findByValue(value);
        User user = byValue.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "hello";
    }

    @PreAuthorize("hasAnyRole()")
    @GetMapping("/test")
    public String test() {
        return "hello";
    }
}
