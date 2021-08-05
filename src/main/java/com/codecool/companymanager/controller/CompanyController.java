package com.codecool.companymanager.controller;

import com.codecool.companymanager.model.dto.CompanyDto;
import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.mapper.CompanyMapper;
import com.codecool.companymanager.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<CompanyDto> getAll() {
        return companyMapper.companiesToDtos(companyService.findAll());
    }

    @GetMapping("/{id}")
    public CompanyDto getById(@PathVariable long id) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id " + id + " not found"));
        return companyMapper.companyToDto(company);
    }

    @PostMapping
    public CompanyDto addNew(@RequestBody CompanyDto companyDto) {
        Company company = companyService.save(companyMapper.companyDtoToCompany(companyDto));
        return companyMapper.companyToDto(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody CompanyDto companyDto) {
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
        }
    }

}
