package ru.foodlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.foodlog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
