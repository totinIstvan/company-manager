package com.codecool.companymanager.unit;

import com.codecool.companymanager.controller.EmployeeController;
import com.codecool.companymanager.model.dto.EmployeeDto;
import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.entity.Employee;
import com.codecool.companymanager.model.mapper.EmployeeMapper;
import com.codecool.companymanager.model.mapper.EmployeeMapperImpl;
import com.codecool.companymanager.service.CompanyService;
import com.codecool.companymanager.service.DepartmentService;
import com.codecool.companymanager.service.EmployeeService;
import com.codecool.companymanager.unit.converter.JsonSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeMapper employeeMapper;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private DepartmentService departmentService;

    private Employee testEmployee;
    private Company testCompany;
    private Department testDepartment;

    @BeforeEach
    private void init() {
        this.testCompany = new Company(1L,
                "TestCompanyName",
                "TestRegNumber",
                "TestAddress",
                "+1 111 111 1111");

        this.testDepartment = new Department(1L, "TestDepartmentName1");

        testCompany.setDepartments(new ArrayList<>(List.of(testDepartment)));

        this.testEmployee = new Employee(1L,
                "testEmployeeName1",
                "testTitle1",
                "testEmail1@testCompany.com",
                10000,
                LocalDateTime.of(2000, 12, 12, 12, 12, 12),
                testDepartment,
                testCompany);
    }
    
    @Test
    public void getAll_returnsValidListOfEmployees() throws Exception {
        EmployeeMapper employeeMapper1 = new EmployeeMapperImpl();
        EmployeeDto employee = employeeMapper1.employeeToDto(testEmployee);
        List<EmployeeDto> employees = List.of(employee);

        when(employeeMapper.employeesToSummaryDtos(service.findAll())).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testEmployee.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(testEmployee.getName())))
                .andExpect(jsonPath("$[0].title", is(testEmployee.getTitle())))
                .andExpect(jsonPath("$[0].email", is(testEmployee.getEmail())))
                .andExpect(jsonPath("$[0].salary", is(testEmployee.getSalary())))
                .andExpect(jsonPath("$[0].joinDate", is (testEmployee.getJoinDate().toString())));
    }

    @Test
    public void getById_returnsValidEmployee() throws Exception {
        EmployeeMapper employeeMapper1 = new EmployeeMapperImpl();
        Optional<Employee> employee = Optional.of(testEmployee);
        EmployeeDto employeeDto = employeeMapper1.employeeToDto(employee.get());
        when(service.findById(1)).thenReturn(employee);
        when(employeeMapper.employeeToSummaryDto(employee.get())).thenReturn(employeeDto);

        String param = "1";
        mockMvc.perform(get("/api/employees/{id}", param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testEmployee.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testEmployee.getName())))
                .andExpect(jsonPath("$.title", is(testEmployee.getTitle())))
                .andExpect(jsonPath("$.email", is(testEmployee.getEmail())))
                .andExpect(jsonPath("$.salary", is(testEmployee.getSalary())))
                .andExpect(jsonPath("$.joinDate", is (testEmployee.getJoinDate().toString())));
    }

    @Test
    public void getById_requestForInvalidId_returnsHttpStatusCode404NOT_FOUND() throws Exception {
        String param = "100";
        mockMvc.perform(get("/api/employees/{id}", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("Employee with id 100 not found", result.getResponse().getErrorMessage()));
    }

    @Test
    public void addNew_whenEmployeePosted_thenReturnsWithValidEmployee() throws Exception {
        EmployeeMapper employeeMapper1 = new EmployeeMapperImpl();
        EmployeeDto employeeDto = employeeMapper1.employeeToDto(testEmployee);
        when(employeeMapper.employeeToDto(service.save(testEmployee))).thenReturn(employeeDto);
        when(companyService.findById(testCompany.getId())).thenReturn(Optional.of(testCompany));
        when(departmentService.findById(testDepartment.getId())).thenReturn(Optional.of(testDepartment));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(testEmployee)))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(testEmployee.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testEmployee.getName())))
                .andExpect(jsonPath("$.title", is(testEmployee.getTitle())))
                .andExpect(jsonPath("$.email", is(testEmployee.getEmail())))
                .andExpect(jsonPath("$.salary", is(testEmployee.getSalary())))
                .andExpect(jsonPath("$.joinDate", is (testEmployee.getJoinDate().toString())));
    }

    @Test
    public void addNew_withEmptyName_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        testEmployee.setName("");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(testEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void addNew_withInvalidEmail_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        testEmployee.setEmail("something&anything_com");
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(testEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void addNew_withNegativeSalary_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        testEmployee.setSalary(-10000);
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(testEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void createNewDepartmentForCompanyIfNecessary_savingEmployeeWithNewDepartment_persistsDepartmentIfNotExists() throws Exception {
        List<Department> allDepartments = testCompany.getDepartments();
        Department newDepartment = new Department(2L, "Brand New Department");
        testEmployee.setDepartment(newDepartment);
        EmployeeMapper employeeMapper1 = new EmployeeMapperImpl();
        EmployeeDto employeeDto = employeeMapper1.employeeToDto(testEmployee);

        when(employeeMapper.employeeToDto(service.save(testEmployee))).thenReturn(employeeDto);
        when(companyService.findById(testCompany.getId())).thenReturn(Optional.of(testCompany));
        when(departmentService.findById(newDepartment.getId())).thenReturn(Optional.of(newDepartment));
        when(companyService.update(testCompany)).thenReturn(testCompany);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(testEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

        List<Department> expected = List.of(testDepartment, newDepartment);

        assertThat(allDepartments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expected);
    }
}
