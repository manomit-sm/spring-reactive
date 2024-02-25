package com.bsolz.userservice.services;

import com.bsolz.userservice.dtos.UserDto;
import com.bsolz.userservice.repositories.UserRepository;
import com.bsolz.userservice.utils.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public Flux<UserDto> allUser() {
        return userRepository.findAll()
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> findByUserId(final int userId) {
        return userRepository.findById(userId)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
        return userDtoMono.map(EntityDtoUtil::toEntity)
                .flatMap(userRepository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<UserDto> updateUser(int userId, Mono<UserDto> userDtoMono) {
        return userRepository.findById(userId)
                .flatMap(u -> userDtoMono.map(EntityDtoUtil::toEntity).doOnNext(e -> e.setId(userId)))
                .flatMap(userRepository::save)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<Void> deleteUser(int userId) {
        return userRepository.deleteById(userId);
    }
}
