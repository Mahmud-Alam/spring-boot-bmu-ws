package com.mahmudalam.bmu_ws.repository;

import com.mahmudalam.bmu_ws.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE LOWER(i.altText) = LOWER(:altText)")
    Optional<Image> findByAltTextIgnoreCase(@Param("altText") String altText);
}
