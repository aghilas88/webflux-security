package com.agh.webfluxmongo.config;

import com.agh.webfluxmongo.dto.User;
import com.agh.webfluxmongo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
@Log4j2
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        userRepository.deleteAll()
                .thenMany(Flux.just("user", "admin")
                        .map(userName -> User.builder()
                                .username(userName)
                                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userName))
                                .active(true)
                                .build())
                        .flatMap(userRepository::save))
                .thenMany(userRepository.findAll())
                .subscribe(user -> log.info("user on database : {}", user));


    }
}
