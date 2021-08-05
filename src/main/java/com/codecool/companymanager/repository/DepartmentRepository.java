package com.codecool.companymanager.repository;

import com.codecool.companymanager.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
