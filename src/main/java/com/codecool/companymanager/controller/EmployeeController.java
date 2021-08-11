package com.codecool.companymanager.controller;

import com.codecool.companymanager.model.dto.EmployeeDto;
import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import com.codecool.companymanager.model.mapper.EmployeeMapper;
import com.codecool.companymanager.service.CompanyService;
import com.codecool.companymanager.service.DepartmentService;
import com.codecool.companymanager.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    private final CompanyService companyService;

    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService,
                              EmployeeMapper employeeMapper,
                              CompanyService companyService,
                              DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.companyService = companyService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<EmployeeDto> getAll(@RequestParam(required = false) Boolean full) {
        List<Employee> employees = employeeService.findAll();
        return mapEmployees(employees, full);
    }

    private List<EmployeeDto> mapEmployees(List<Employee> employees, Boolean full) {
        if (full == null || !full) {
            return employeeMapper.employeesToSummaryDtos(employees);
        } else {
            return employeeMapper.employeesToDtos(employees);
        }
    }

    @GetMapping("/{id}")
    public EmployeeDto getById(@PathVariable long id, @RequestParam(required = false) Boolean full) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with id " + id + " not found"));
        if (full == null || !full) {
            return employeeMapper.employeeToSummaryDto(employee);
        }
        return employeeMapper.employeeToDto(employee);
    }

    @PostMapping
    public EmployeeDto addNew(@RequestBody @Valid EmployeeDto employeeDto) {
        try {
            createNewDepartmentForCompanyIfNecessary(employeeDto);
            return employeeMapper.employeeToDto(employeeService.save(employeeMapper.employeeDtoToEmployee(employeeDto)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
        try {
            createNewDepartmentForCompanyIfNecessary(employeeDto);
            Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto);
            EmployeeDto savedEmployeeDto = employeeMapper.employeeToDto(employeeService.update(employee));
            return ResponseEntity.ok(savedEmployeeDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with id " + id + " not found");
        }
    }

    private void createNewDepartmentForCompanyIfNecessary(@RequestBody @Valid EmployeeDto employeeDto) {
        Company company = companyService.findById(employeeDto.getCompany().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with requested id does not exist"));
        Department department = departmentService.findById(employeeDto.getDepartment().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with requested id does not exist"));
        if (!company.getDepartments().contains(department)) {
            company.getDepartments().add(department);
            companyService.update(company);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            employeeService.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with id " + id + " not found");
        }
    }

    @GetMapping("/limit")
    public List<EmployeeDto> getWithSalaryHigherThan(@RequestParam int limit) {
        List<Employee> employees = employeeService.getWithSalaryHigherThan(limit);
        return employeeMapper.employeesToSummaryDtos(employees);
    }
}
