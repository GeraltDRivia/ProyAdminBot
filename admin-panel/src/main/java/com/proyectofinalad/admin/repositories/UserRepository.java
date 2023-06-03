package com.proyectofinalad.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectofinalad.admin.models.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
