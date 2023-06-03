package com.proyectofinalad.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectofinalad.admin.models.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
