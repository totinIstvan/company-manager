package com.codecool.companymanager.integration;

import com.codecool.companymanager.model.entity.Department;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class DepartmentIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Department testDepartment1;
    private Department testDepartment2;

    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port + "/api/departments";
        this.testDepartment1 = new Department(1L, "TestDepartmentName1");
        this.testDepartment2 = new Department(2L, "TestDepartmentName2");
    }

    @Test
    public void addNew_addDepartmentToEmptyDatabase_shouldReturnSameDepartment() {
        Department addedDepartment = testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);

        assertThat(testDepartment1)
                .usingRecursiveComparison()
                .isEqualTo(addedDepartment);
    }

    @Test
    public void addNew_addDepartmentWithEmptyName_shouldReturnHttpStatus400BAD_REQUEST() {
        Department invalidDepartment = new Department(1L, "");

        ResponseEntity<Department> response = testRestTemplate.postForEntity(baseUrl, invalidDepartment, Department.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addNew_addDepartmentTwiceToDatabase_shouldReturnHttpStatus409CONFLICT() {
        testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);

        ResponseEntity<Department> response = testRestTemplate.postForEntity(baseUrl, testDepartment1, Department.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void getAll_emptyDatabase_shouldReturnEmptyList() {
        List<Department> departments = List.of(testRestTemplate.getForObject(baseUrl, Department[].class));

        assertTrue(departments.isEmpty());
    }

    @Test
    public void getAll_addedDepartments_shouldReturnListOfDepartments() {
        testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);
        testRestTemplate.postForObject(baseUrl, testDepartment2, Department.class);
        List<Department> testDepartments = new ArrayList<>(Arrays.asList(testDepartment1, testDepartment2));

        List<Department> allDepartments = List.of(testRestTemplate.getForObject(baseUrl, Department[].class));

        assertThat(testDepartments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(allDepartments);
    }

    @Test
    public void getById_addedDepartments_shouldReturnCorrectDepartment() {
        testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);
        testRestTemplate.postForObject(baseUrl, testDepartment2, Department.class);

        Department result = testRestTemplate.getForObject(baseUrl + "/" + testDepartment1.getId(), Department.class);

        assertThat(testDepartment1)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    public void getById_addedDepartmentsRequestForInvalidId_returnsHttpStatusCode404NOT_FOUND() {
        testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);
        testRestTemplate.postForObject(baseUrl, testDepartment2, Department.class);

        ResponseEntity<Department> response = testRestTemplate.getForEntity(baseUrl + "/" + 0, Department.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_addedDepartment_getByIdReturnsUpdatedDepartment() {
        Department addedDepartment = testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);

        addedDepartment.setName("UpdatedDepartmentName");
        testRestTemplate.put(baseUrl + "/" + addedDepartment.getId(), addedDepartment);
        Department updatedDepartment = testRestTemplate.getForObject(baseUrl + "/" + addedDepartment.getId(), Department.class);

        assertThat(addedDepartment)
                .usingRecursiveComparison()
                .isEqualTo(updatedDepartment);
    }

    @Test
    public void delete_addedDepartments_getAllReturnsRemainingDepartments() {
        testRestTemplate.postForObject(baseUrl, testDepartment1, Department.class);
        testRestTemplate.postForObject(baseUrl, testDepartment2, Department.class);
        List<Department> testDepartments = new ArrayList<>(Arrays.asList(testDepartment1, testDepartment2));

        int index = 0;
        Department department = testDepartments.get(index);
        testRestTemplate.delete(baseUrl + "/" + department.getId());
        testDepartments.remove(index);

        List<Department> remainingDepartments = List.of(testRestTemplate.getForObject(baseUrl, Department[].class));

        assertThat(testDepartments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(remainingDepartments);
    }
}
