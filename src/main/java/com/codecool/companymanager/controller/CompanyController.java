package com.codecool.companymanager.controller;

import com.codecool.companymanager.model.dto.CompanyDto;
import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.mapper.CompanyMapper;
import com.codecool.companymanager.service.CompanyService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyDto> getAll(@RequestParam(required = false) Boolean full) {
        List<Company> companies = companyService.findAll();
        return mapCompanies(companies, full);
    }

    @GetMapping(path = "/{id}")
    public CompanyDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id " + id + " not found"));
        if (full == null || !full) {
            return companyMapper.companyToSummaryDto(company);
        }
        return companyMapper.companyToDto(company);
    }

    @PostMapping
    public CompanyDto addNew(@RequestBody @Valid CompanyDto companyDto) {
        try {
            Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
            return companyMapper.companyToDto(company);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody @Valid CompanyDto companyDto) {
        Company company = companyMapper.companyDtoToCompany(companyDto);
        company.setId(id);
        try {
            CompanyDto savedCompanyDto = companyMapper.companyToDto(companyService.update(company));
            return ResponseEntity.ok(savedCompanyDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            companyService.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id " + id + " not found");
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company with id " + id + " cannot be deleted from the database because it still has existing departments and/or employees");
        }
    }

    private List<CompanyDto> mapCompanies(List<Company> companies, Boolean full) {
        if (full == null || !full) {
            return companyMapper.companiesToSummaryDtos(companies);
        } else {
            return companyMapper.companiesToDtos(companies);
        }
    }
}
