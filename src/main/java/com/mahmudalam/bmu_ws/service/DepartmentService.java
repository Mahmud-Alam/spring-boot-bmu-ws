package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.Department;
import com.mahmudalam.bmu_ws.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;


    public UserResponse<Page<Department>> getAllDepartments(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Department> allDepts = departmentRepository.findAll(pageable);
            return new UserResponse<>(true, allDepts, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch departments: " + e.getMessage());
        }
    }

    public UserResponse<Department> createDepartment(Department createdDepartment) {
        try {
            createdDepartment.setName(createdDepartment.getName().toUpperCase());
            createdDepartment.setCode(createdDepartment.getCode().toLowerCase());

            Department created = departmentRepository.save(createdDepartment);

            return new UserResponse<>(true, created, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create department: " + e.getMessage());
        }
    }

    public UserResponse<Department> patchUpdateDepartment(Long id, Department updatedDepartment) {
        try {
            return departmentRepository.findById(id)
                    .map(existing -> {
                        existing.setName(updatedDepartment.getName() != null ? updatedDepartment.getName().toUpperCase() : existing.getName());
                        existing.setCode(updatedDepartment.getCode() != null ? updatedDepartment.getCode().toLowerCase() : existing.getCode());
                        existing.setDescription(updatedDepartment.getDescription() != null ? updatedDepartment.getDescription() : existing.getDescription());

                        departmentRepository.save(existing);
                        return new UserResponse<>(true, existing, null);
                    })
                    .orElse(new UserResponse<>(false, null, "Department not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed update: " + e.getMessage());
        }
    }


    public UserResponse<String> deleteDepartment(Long id) {
        try {
            return departmentRepository.findById(id)
                    .map(department -> {
                        departmentRepository.delete(department);

                        return new UserResponse<>(true, "Department deleted successfully.", null);
                    })
                    .orElse(new UserResponse<>(false, null, "Department not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to delete department: " + e.getMessage());
        }
    }

    public UserResponse<String> changeActiveStatus(Long id) {
        try {
            return departmentRepository.findById(id)
                    .map(department -> {
                        department.setIsActive(!department.getIsActive());

                        departmentRepository.save(department);

                        return new UserResponse<>(true, "Change Active Status: " + department.getIsActive(), null);
                    })
                    .orElse(new UserResponse<>(false, null, "Department not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to change active status: " + e.getMessage());
        }
    }
}
