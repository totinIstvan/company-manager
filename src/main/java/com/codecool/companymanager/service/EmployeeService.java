package com.codecool.companymanager.service;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import com.codecool.companymanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee save(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            throw new IllegalArgumentException("Employee with requested id already exists, please try the update function");
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            return employeeRepository.save(employee);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getWithSalaryHigherThan(int limit) {
        return employeeRepository.findEmployeesBySalaryAfter(limit);
    }

    public List<Employee> getAllEmployeesOfGivenCompany(Company company) {
        return employeeRepository.findAllEmployeesByCompany(company);
    }

    public List<Employee> getAllEmployeesOfGivenCompanyAndDepartment(Company company, Department department) {
        return employeeRepository.findAllEmployeesByCompanyAndDepartment(company, department);
    }
}
