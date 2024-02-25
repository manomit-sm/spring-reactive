package com.bsolz.userservice.repositories;

import com.bsolz.userservice.entities.UserTransaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserTransactionRepository extends ReactiveCrudRepository<UserTransaction, Integer> {
}
