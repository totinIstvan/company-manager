package com.codecool.companymanager.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String title;

    private String email;

    private int salary;

    private LocalDateTime joinDate;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Company company;

    public Employee() {
    }

    public Employee(Long id,
                    String name,
                    String title,
                    String email,
                    int salary,
                    LocalDateTime joinDate,
                    Department department,
                    Company company) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.email = email;
        this.salary = salary;
        this.joinDate = joinDate;
        this.department = department;
        this.company = company;
    }

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
