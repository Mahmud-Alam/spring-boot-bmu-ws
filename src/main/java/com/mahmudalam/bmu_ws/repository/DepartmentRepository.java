package com.mahmudalam.bmu_ws.repository;

import com.mahmudalam.bmu_ws.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
