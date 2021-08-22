package com.codecool.companymanager.unit;

import com.codecool.companymanager.controller.CompanyController;
import com.codecool.companymanager.model.dto.CompanyDto;
import com.codecool.companymanager.model.dto.DepartmentDto;
import com.codecool.companymanager.model.entity.Company;
import com.codecool.companymanager.model.mapper.CompanyMapper;
import com.codecool.companymanager.model.mapper.CompanyMapperImpl;
import com.codecool.companymanager.model.mapper.EmployeeMapper;
import com.codecool.companymanager.service.CompanyService;
import com.codecool.companymanager.service.DepartmentService;
import com.codecool.companymanager.service.EmployeeService;
import com.codecool.companymanager.unit.converter.JsonSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService service;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeMapper employeeMapper;

    @MockBean
    private CompanyMapper companyMapper;

    @Test
    public void getAll_returnsValidListOfCompanies() throws Exception {
        CompanyDto company = new CompanyDto(1L, "Test Company", "Test Registration Number", "Test Address", "+1 111 111 1111");
        List<CompanyDto> companies = List.of(company);

        when(companyMapper.companiesToSummaryDtos(service.findAll())).thenReturn(companies);

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(company.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(company.getName())))
                .andExpect(jsonPath("$[0].registrationNumber", is(company.getRegistrationNumber())))
                .andExpect(jsonPath("$[0].address", is(company.getAddress())))
                .andExpect(jsonPath("$[0].phoneNumber", is(company.getPhoneNumber())));
    }

    @Test
    public void getById_returnsValidCompany() throws Exception {
        CompanyMapper companyMapper1 = new CompanyMapperImpl();
        Optional<Company> company = Optional.of(new Company(1L, "Test Company", "Test Registration Number", "Test Address", "+1 111 111 1111"));
        CompanyDto companyDto = companyMapper1.companyToDto(company.get());
        when(service.findById(1)).thenReturn(company);
        when(companyMapper.companyToSummaryDto(company.get())).thenReturn(companyDto);

        String param = "1";
        mockMvc.perform(get("/api/companies/{id}", param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(companyDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.registrationNumber", is(companyDto.getRegistrationNumber())))
                .andExpect(jsonPath("$.address", is(companyDto.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(companyDto.getPhoneNumber())));
    }

    @Test
    public void getById_requestParamFullEqualsTrue_returnsValidCompanyWithDepartments() throws Exception {
        CompanyMapper companyMapper1 = new CompanyMapperImpl();
        Optional<Company> company = Optional.of(new Company(1L, "Test Company", "Test Registration Number", "Test Address", "+1 111 111 1111"));
        DepartmentDto department = new DepartmentDto(1L, "Test Department");
        List<DepartmentDto> departments = List.of(department);
        CompanyDto companyDto = companyMapper1.companyToDto(company.get());
        companyDto.setDepartments(departments);

        when(service.findById(1)).thenReturn(company);
        when(companyMapper.companyToDto(company.get())).thenReturn(companyDto);

        String param = "1";
        mockMvc.perform(get("/api/companies/{id}?full=true", param))
                .andExpect(jsonPath("$.id", is(companyDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.registrationNumber", is(companyDto.getRegistrationNumber())))
                .andExpect(jsonPath("$.address", is(companyDto.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(companyDto.getPhoneNumber())))
                .andExpect(jsonPath("$.departments").exists())
                .andExpect(jsonPath("$.departments[*].name").isNotEmpty());
    }

    @Test
    public void getById_requestForInvalidId_returnsHttpStatusCode404NOT_FOUND() throws Exception {
        String param = "100";
        mockMvc.perform(get("/api/companies/{id}", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("Company with id 100 not found", result.getResponse().getErrorMessage()));
    }

    @Test
    public void addNew_whenCompanyPosted_thenReturnsWithValidCompany() throws Exception {
        CompanyMapper companyMapper1 = new CompanyMapperImpl();
        Company company = new Company(1L, "Test Company", "Test Registration Number", "Test Address", "+1 111 111 1111");
        CompanyDto companyDto = companyMapper1.companyToDto(company);
        when(companyMapper.companyToDto(service.save(company))).thenReturn(companyDto);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(company)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(company.getId().intValue())))
                .andExpect(jsonPath("$.name", is(company.getName())))
                .andExpect(jsonPath("$.registrationNumber", is(company.getRegistrationNumber())))
                .andExpect(jsonPath("$.address", is(company.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(company.getPhoneNumber())));
    }

    @Test
    public void addNew_withEmptyName_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        Company company = new Company(1L, "", "Test Registration Number", "Test Address", "+1 111 111 1111");
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(company)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void addNew_withInvalidPhoneNumber_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        Company company = new Company(1L, "Test Company", "Test Registration Number", "Test Address", "+10 1234567 111 1111");
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(company)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void addNew_withEmptyRegistrationNumber_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        Company company = new Company(1L, "Test Company", "    ", "Test Address", "+1 111 111 1111");
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(company)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void addNew_withEmptyAddress_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        Company company = new Company(1L, "Test Company", "Test Registration Number", "", "+1 111 111 1111");
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonSerializer.asJsonString(company)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}
