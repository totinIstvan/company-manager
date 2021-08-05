package com.codecool.companymanager.repository;

import com.codecool.companymanager.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}