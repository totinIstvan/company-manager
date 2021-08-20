package com.codecool.companymanager.integration;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import com.codecool.companymanager.repository.CompanyRepository;
import com.codecool.companymanager.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class EmployeeIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee testEmployee1;
    private Employee testEmployee2;
    private Employee testEmployee3;

    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port + "/api/companies";

        Company testCompany = new Company(1L,
                "TestCompanyName",
                "TestRegNumber",
                "TestAddress",
                "+1 111 111 1111");
        companyRepository.save(testCompany);

        Department testDepartment = new Department(1L, "TestDepartmentName1");
        departmentRepository.save(testDepartment);

        this.testEmployee1 = new Employee(1L,
                "testEmployeeName1",
                "testTitle1",
                "testEmail1@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);

        this.testEmployee2 = new Employee(2L,
                "testEmployeeName2",
                "testTitle2",
                "testEmail2@testCompany.com",
                11000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);

        this.testEmployee3 = new Employee(3L,
                "testEmployeeName3",
                "testTitle3",
                "testEmail3@testCompany.com",
                12000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);
    }

    @Test
    public void addNew_() {

    }
}
