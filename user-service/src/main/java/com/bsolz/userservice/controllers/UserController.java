package com.bsolz.userservice.controllers;

import com.bsolz.userservice.dtos.UserDto;
import com.bsolz.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public Flux<UserDto> allUser() {
        return userService.allUser();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable int id) {
        return userService.findByUserId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<UserDto> createUser(@RequestBody Mono<UserDto> userDtoMono) {
        return userService.createUser(userDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable int id, @RequestBody Mono<UserDto> userDtoMono) {
        return userService.updateUser(id, userDtoMono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }
}
