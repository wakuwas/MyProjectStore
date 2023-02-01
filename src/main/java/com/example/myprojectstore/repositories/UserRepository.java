package com.example.myprojectstore.repositories;

import com.example.myprojectstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);


}
