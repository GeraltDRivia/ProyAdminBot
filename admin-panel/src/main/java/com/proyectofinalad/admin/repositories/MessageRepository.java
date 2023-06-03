package com.proyectofinalad.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectofinalad.admin.models.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
