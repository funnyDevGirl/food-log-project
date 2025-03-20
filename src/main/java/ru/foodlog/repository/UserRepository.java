package ru.foodlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.foodlog.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.email = :email")
    Optional<User> findByEmailWithEagerUpload(@Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.meals WHERE u.id = :id")
    Optional<User> findByIdWithEagerUpload(@Param("id") Long id);
}
