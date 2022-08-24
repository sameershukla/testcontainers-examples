package com.example.application.db.repository;

import com.example.application.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);


}
