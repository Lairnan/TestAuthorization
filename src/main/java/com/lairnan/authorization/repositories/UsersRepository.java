package com.lairnan.authorization.repositories;

import com.lairnan.authorization.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String str);
}
