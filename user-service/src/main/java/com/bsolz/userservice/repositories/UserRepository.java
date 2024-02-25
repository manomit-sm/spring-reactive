package com.bsolz.userservice.repositories;

import com.bsolz.userservice.entities.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    @Modifying
    @Query("UPDATE users SET balance = balance - :amount WHERE id = :userid AND balance >= :amount")
    Mono<Boolean> updateUserBalance(int userid, int amount);
}
