package com.codecool.companymanager.model.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @Email(message = "Invalid email address")
    private String email;

    @Min(0)
    private int salary;

    @Past(message = "Invalid join date")
    private LocalDateTime joinDate;

    @NotNull(message = "Department is mandatory")
    private DepartmentDto department;

    @NotNull(message = "Company is mandatory")
    private CompanyDto company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }
}
