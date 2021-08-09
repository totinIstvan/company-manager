package com.codecool.companymanager.repository;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findEmployeesBySalaryAfter(int limit);

    List<Employee> findAllEmployeesByCompany(Company company);

    List<Employee> findAllEmployeesByCompanyAndDepartment(Company company, Department department);
}
