package com.mahmudalam.bmu_ws.repository;

import com.mahmudalam.bmu_ws.model.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {

    Optional<Collaboration> findByTitle(String title);
}
