package com.agh.webfluxmongo.controllers;

import com.agh.webfluxmongo.config.JWTUtil;
import com.agh.webfluxmongo.dto.User;
import com.agh.webfluxmongo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private JWTUtil jwtUtil;

    @PostMapping("/token")
    public Mono<ResponseEntity<String>> getToken (@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername())
             .map(userDetails -> {
                 if (user.getPassword().equals(userDetails.getPassword())) {
                     return ResponseEntity
                             .ok(jwtUtil.generateToken(user));
                 } else {
                     return ResponseEntity.badRequest().body("invalid credentials");
                 }
             }).switchIfEmpty(Mono.just(ResponseEntity.badRequest().body("invalid credentials")));
    }

    @GetMapping("/token")
    public Mono<ResponseEntity<String>> test () {
        return Mono.just(ResponseEntity.ok("test"));
    }

}
