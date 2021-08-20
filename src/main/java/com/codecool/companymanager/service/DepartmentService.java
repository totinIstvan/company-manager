package com.codecool.companymanager.service;

import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(long id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public Department save(Department department) {
        if (departmentRepository.existsById(department.getId())) {
            throw new IllegalArgumentException("Department with requested id already exists, please try the update function");
        }
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(Department department) {
        if (departmentRepository.existsById(department.getId())) {
            return departmentRepository.save(department);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}
