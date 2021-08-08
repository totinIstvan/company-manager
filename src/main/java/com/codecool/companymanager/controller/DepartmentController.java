package com.codecool.companymanager.controller;

import com.codecool.companymanager.model.dto.DepartmentDto;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.mapper.DepartmentMapper;
import com.codecool.companymanager.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    private  final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public List<DepartmentDto> getAll() {
        return departmentMapper.departmentsToDtos(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public DepartmentDto getById(@PathVariable long id) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with id " + id + " not found"));
        return departmentMapper.departmentToDto(department);
    }

    @PostMapping
    public DepartmentDto addNew(@RequestBody DepartmentDto departmentDto) {
        try {
            Department department = departmentService.save(departmentMapper.departmentDtoToDepartment(departmentDto));
            return departmentMapper.departmentToDto(department);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable long id, @RequestBody DepartmentDto departmentDto) {
        Department department = departmentMapper.departmentDtoToDepartment(departmentDto);
        department.setId(id);
        try {
            DepartmentDto savedDepartmentDto = departmentMapper.departmentToDto(departmentService.update(department));
            return ResponseEntity.ok(savedDepartmentDto);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        try {
            departmentService.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with id " + id + " not found");
        }
    }
}
