package com.example.myprojectstore.repositories;

import com.example.myprojectstore.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
