package com.codecool.companymanager.integration;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import com.codecool.companymanager.repository.DepartmentRepository;
import com.codecool.companymanager.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CompanyIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    private Company testCompany1;
    private List<Company> allCompanies;
    private Department testDepartment1;
    private Department testDepartment2;
    private Employee testEmployee1;
    private Employee testEmployee2;
    private Employee testEmployee3;

    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port + "/api/companies";

        this.testCompany1 = new Company(1L,
                "TestCompanyName1",
                "TestRegNumber1",
                "TestAddress1",
                "+1 111 111 1111");

        Company testCompany2 = new Company(2L,
                "testCompanyName2",
                "TestRegNumber2",
                "TestAddress2",
                "+1 234 567 2222");

        Company testCompany3 = new Company(3L,
                "testCompanyName3",
                "TestRegNumber3",
                "TestAddress3",
                "+1 234 567 3333");

        this.allCompanies = new ArrayList<>(Arrays.asList(testCompany1, testCompany2, testCompany3));

        this.testDepartment1 = new Department(1L, "TestDepartmentName1");
        this.testDepartment2 = new Department(2L, "TestDepartmentName2");
        departmentRepository.save(testDepartment1);
        departmentRepository.save(testDepartment2);

        this.testEmployee1 = new Employee(1L,
                "testEmployeeName1",
                "testTitle1",
                "testEmail1@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment1,
                testCompany1);

        this.testEmployee2 = new Employee(2L,
                "testEmployeeName2",
                "testTitle2",
                "testEmail2@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment2,
                testCompany1);

        this.testEmployee3 = new Employee(3L,
                "testEmployeeName3",
                "testTitle3",
                "testEmail3@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment2,
                testCompany1);
    }

    @Test
    public void addNew_addCompanyToEmptyDatabaseWithoutDepartments_shouldReturnSameCompany() {
        Company result = testRestTemplate.postForObject(baseUrl, testCompany1, Company.class);

        assertThat(testCompany1)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void addNew_addCompanyToEmptyDatabaseWithDepartments_shouldReturnSameCompany() {
        this.testCompany1.setDepartments(Arrays.asList(testDepartment1, testDepartment2));
        Company result = testRestTemplate.postForObject(baseUrl, testCompany1, Company.class);

        assertThat(testCompany1)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void getAll_emptyDatabase_shouldReturnEmptyList() {
        List<Company> companies = List.of(testRestTemplate.getForObject(baseUrl, Company[].class));
        assertTrue(companies.isEmpty());
    }

    @Test
    public void getAll_addedSomeCompaniesWithDepartmentsIfRequestParamFullEqualsFalse_shouldReturnAllCompaniesWithoutDepartments() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        List<Company> resultCompanies = List.of(testRestTemplate.getForObject(baseUrl + "?full=false", Company[].class));

        for (int i = 0; i < resultCompanies.size(); i++) {
            assertEquals(allCompanies.get(i).getName(), resultCompanies.get(i).getName());
            assertEquals(allCompanies.get(i).getAddress(), resultCompanies.get(i).getAddress());
            assertEquals(allCompanies.get(i).getPhoneNumber(), resultCompanies.get(i).getPhoneNumber());
            assertEquals(allCompanies.get(i).getRegistrationNumber(), resultCompanies.get(i).getRegistrationNumber());
            assertNull(resultCompanies.get(i).getDepartments());
        }
    }

    @Test
    public void getAll_addedSomeCompaniesWithDepartmentsIfRequestParamFullEqualsTrue_shouldReturnAllCompaniesWithDepartments() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        List<Company> resultCompanies = List.of(testRestTemplate.getForObject(baseUrl + "?full=true", Company[].class));

        assertThat(allCompanies)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(resultCompanies);
    }

    @Test
    public void getById_emptyDatabase_returnsHttpStatusCode404NOT_FOUND() {
        ResponseEntity<Company> response = testRestTemplate.getForEntity(baseUrl + "/" + 1, Company.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getById_addedSomeCompaniesWithDepartmentsIfRequestParamFullEqualsFalse_shouldReturnCompanyWithoutDepartments() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        Company expected = allCompanies.get(0);

        Company result = testRestTemplate.getForObject(baseUrl + "/" + expected.getId() + "?full=false", Company.class);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getAddress(), result.getAddress());
        assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expected.getRegistrationNumber(), result.getRegistrationNumber());
        assertNull(result.getDepartments());
    }

    @Test
    public void getById_addedSomeCompaniesWithDepartmentsIfRequestParamFullEqualsTrue_shouldReturnSameCompanyWithDepartments() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        Company expected = allCompanies.get(0);

        Company result = testRestTemplate.getForObject(baseUrl + "/" + expected.getId() + "?full=true", Company.class);

        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void update_addedSomeCompanies_getByIdReturnsUpdatedCompany() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        Company company = allCompanies.get(0);

        company.setName("updatedTestName");
        company.setAddress("updatedTestAddress");
        company.setPhoneNumber("+1 222 333 4444");
        testRestTemplate.put(baseUrl + "/" + company.getId(), company);
        Company updatedCompany = testRestTemplate.getForObject(baseUrl + "/" + company.getId() + "?full=true", Company.class);

        assertThat(company)
                .usingRecursiveComparison()
                .isEqualTo(updatedCompany);
    }

    @Test
    public void delete_fromAddedCompanies_getAllReturnsRemainingCompanies() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));

        Company company = allCompanies.get(1);
        testRestTemplate.delete(baseUrl + "/" + company.getId());
        allCompanies.remove(company);

        List<Company> remainingCompanies = List.of(testRestTemplate.getForObject(baseUrl + "?full=true", Company[].class));

        assertThat(allCompanies)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(remainingCompanies);
    }

    @Test
    public void getAllEmployeesOfTheCompany_requestForAllEmployeesByCompanyId_returnsListOfAllEmployees() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));
        employeeRepository.save(testEmployee1);
        employeeRepository.save(testEmployee2);
        employeeRepository.save(testEmployee3);

        Employee[] source = {testEmployee1, testEmployee2, testEmployee3};
        Employee[] allEmployeesOfCompany = testRestTemplate.getForObject(baseUrl + "/" + allCompanies.get(0).getId() + "/employees", Employee[].class);

        for (int i = 0; i < allEmployeesOfCompany.length; i++) {
            assertEquals(source[i].getId(), allEmployeesOfCompany[i].getId());
            assertEquals(source[i].getName(), allEmployeesOfCompany[i].getName());
            assertEquals(source[i].getEmail(), allEmployeesOfCompany[i].getEmail());
            assertEquals(source[i].getJoinDate(), allEmployeesOfCompany[i].getJoinDate());
            assertEquals(source[i].getSalary(), allEmployeesOfCompany[i].getSalary());
            assertEquals(source[i].getTitle(), allEmployeesOfCompany[i].getTitle());
        }
    }

    @Test
    public void getAllEmployeesOfTheCompanyByDepartment_requestForAllEmployeesByCompanyIdAndDepartmentId_returnsListOfEmployees() {
        this.allCompanies.forEach(company -> company.setDepartments(Arrays.asList(testDepartment1, testDepartment2)));
        allCompanies.forEach(company -> testRestTemplate.postForEntity(baseUrl, company, Company.class));
        employeeRepository.save(testEmployee1);
        employeeRepository.save(testEmployee2);
        employeeRepository.save(testEmployee3);

        Employee[] source = {testEmployee2, testEmployee3};
        Employee[] allEmployeesByDepartment = testRestTemplate.getForObject(baseUrl + "/" + allCompanies.get(0).getId() + "/departments" + "/" + testEmployee3.getDepartment().getId() + "/employees", Employee[].class);

        for (int i = 0; i < allEmployeesByDepartment.length; i++) {
            assertEquals(source[i].getId(), allEmployeesByDepartment[i].getId());
            assertEquals(source[i].getName(), allEmployeesByDepartment[i].getName());
            assertEquals(source[i].getEmail(), allEmployeesByDepartment[i].getEmail());
            assertEquals(source[i].getJoinDate(), allEmployeesByDepartment[i].getJoinDate());
            assertEquals(source[i].getSalary(), allEmployeesByDepartment[i].getSalary());
            assertEquals(source[i].getTitle(), allEmployeesByDepartment[i].getTitle());
        }
    }
}
