package com.codecool.companymanager.integration;

import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class EmployeeIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Employee testEmployee1;
    private List<Employee> employees;

    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port + "/api/employees";

        Company testCompany = new Company(1L,
                "TestCompanyName",
                "TestRegNumber",
                "TestAddress",
                "+1 111 111 1111");
        testRestTemplate.postForObject("http://localhost:" + port + "/api/companies", new HttpEntity<>(testCompany), Company.class);

        Department testDepartment = new Department(1L, "TestDepartmentName1");
        testRestTemplate.postForObject("http://localhost:" + port + "/api/departments", new HttpEntity<>(testDepartment), Department.class);

        testCompany.setDepartments(List.of(testDepartment));

        this.testEmployee1 = new Employee(1L,
                "testEmployeeName1",
                "testTitle1",
                "testEmail1@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);

        Employee testEmployee2 = new Employee(2L,
                "testEmployeeName2",
                "testTitle2",
                "testEmail2@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);

        Employee testEmployee3 = new Employee(3L,
                "testEmployeeName3",
                "testTitle3",
                "testEmail3@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);

        this.employees = new ArrayList<>(Arrays.asList(testEmployee1, testEmployee2, testEmployee3));
    }

    @Test
    public void addNew_emptyDatabase_shouldReturnSameEmployee() {
        Employee result = testRestTemplate.postForObject(baseUrl, testEmployee1, Employee.class);

        assertThat(testEmployee1)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void addNew_addEmployeeTwice_shouldReturnHttpStatus409CONFLICT() {
        testRestTemplate.postForObject(baseUrl, testEmployee1, Employee.class);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void addNew_addEmployeeWithEmptyName_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setName("");

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addEmployeeWithInvalidEmail_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setEmail("invalidEmail");

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addEmployeeWithBlankTitle_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setTitle("   ");

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addEmployeeWithInvalidJoinDate_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setJoinDate(LocalDateTime.of(2222, 12, 12, 12, 12, 12));

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addEmployeeWithoutDepartment_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setDepartment(null);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addEmployeeWithoutCompany_shouldReturnHttpStatus400BAD_REQUEST() {
        testEmployee1.setCompany(null);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity(baseUrl, testEmployee1, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAll_emptyDatabase_shouldReturnEmptyList() {
        List<Employee> employees = List.of(testRestTemplate.getForObject(baseUrl, Employee[].class));

        assertTrue(employees.isEmpty());
    }

    @Test
    public void getAll_addedSomeEmployeesWithDepartmentAndCompanyIfRequestParamFullEqualsFalse_shouldReturnAllEmployeesWithoutDepartmentAndCompany() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        List<Employee> result = List.of(testRestTemplate.getForObject(baseUrl + "?full=false", Employee[].class));

        for (int i = 0; i < result.size(); i++) {
            assertEquals(employees.get(i).getName(), result.get(i).getName());
            assertEquals(employees.get(i).getTitle(), result.get(i).getTitle());
            assertEquals(employees.get(i).getSalary(), result.get(i).getSalary());
            assertEquals(employees.get(i).getEmail(), result.get(i).getEmail());
            assertEquals(employees.get(i).getJoinDate(), result.get(i).getJoinDate());
            assertNull(result.get(i).getDepartment());
            assertNull(result.get(i).getCompany());
        }
    }

    @Test
    public void getAll_addedSomeEmployeesWithDepartmentAndCompanyIfRequestParamFullEqualsTrue_shouldReturnAllEmployeesWithDepartmentAndCompany() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        List<Employee> result = List.of(testRestTemplate.getForObject(baseUrl + "?full=true", Employee[].class));

        assertThat(employees)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(result);
    }

    @Test
    public void getById_addedOneEmployeeRequestWithInvalidId_returnsHttpStatusCode404NOT_FOUND() {
        testRestTemplate.postForObject(baseUrl, testEmployee1, Employee.class);
        ResponseEntity<Employee> response = testRestTemplate.getForEntity(baseUrl + "/" + 124567, Employee.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getById_addedSomeEmployeesWithDepartmentAndCompanyIfRequestParamFullEqualsFalse_shouldReturnEmployeeWithoutDepartmentAndCompany() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        Employee expected = employees.get(0);

        Employee result = testRestTemplate.getForObject(baseUrl + "/" + expected.getId() + "?full=false", Employee.class);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getSalary(), result.getSalary());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getJoinDate(), result.getJoinDate());
        assertNull(result.getDepartment());
        assertNull(result.getCompany());
    }

    @Test
    public void getById_addedSomeEmployeesWithDepartmentAndCompanyIfRequestParamFullEqualsTrue_shouldReturnEmployeeWithDepartmentAndCompany() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        Employee expected = employees.get(0);

        Employee result = testRestTemplate.getForObject(baseUrl + "/" + expected.getId() + "?full=true", Employee.class);

        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void update_addedSomeEmployees_getByIdReturnsUpdatedEmployee() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        Employee expected = employees.get(0);

        expected.setName("updatedTestName");
        expected.setTitle("updatedTestTitle");
        expected.setEmail("updated@test.com");
        testRestTemplate.put(baseUrl + "/" + expected.getId(), expected);
        Employee updated = testRestTemplate.getForObject(baseUrl + "/" + expected.getId() + "?full=true", Employee.class);

        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void delete_fromAddedEmployees_getAllReturnsRemainingEmployees() {
        this.employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        Employee expected = employees.get(1);
        testRestTemplate.delete(baseUrl + "/" + expected.getId());
        employees.remove(expected);

        List<Employee> result = List.of(testRestTemplate.getForObject(baseUrl + "?full=true", Employee[].class));

        assertThat(employees)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(result);
    }

    @Test
    public void getWithSalaryHigherThan_addedSomeEmployees_returnsAllEmployeesWithSalaryHigherThanLimit() {
        int payRaise = 600;
        for (Employee e : this.employees) {
            e.setSalary(e.getSalary() + payRaise);
            payRaise *= 2;
        }

        employees.forEach(employee -> testRestTemplate.postForEntity(baseUrl, employee, Employee.class));

        int limit = 11000;
        employees.removeIf(e -> e.getSalary() < limit);

        List<Employee> result = List.of(testRestTemplate.getForObject(baseUrl + "/limit?limit=" + limit, Employee[].class));

        assertThat(employees)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("department", "company")
                .containsExactlyElementsOf(result);
    }
}
