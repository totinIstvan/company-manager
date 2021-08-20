package com.codecool.companymanager.service;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.repository.CompanyRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(long id) {
        return companyRepository.findById(id);
    }

    @Transactional
    public Company save(Company company) {
        if (companyRepository.existsById(company.getId())) {
            throw new IllegalArgumentException("Company with requested id already exists, please try the update function");
        }

        try {
            return companyRepository.save(company);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Company with that name or registration number already exists");
        }
    }

    @Transactional
    public Company update(Company company) {
        if (companyRepository.existsById(company.getId())) {
            return companyRepository.save(company);
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public void deleteById(long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}
