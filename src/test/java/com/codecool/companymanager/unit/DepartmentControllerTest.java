package com.codecool.companymanager.unit;

import com.codecool.companymanager.controller.DepartmentController;
import com.codecool.companymanager.model.dto.DepartmentDto;
import com.codecool.companymanager.model.entity.Department;
import com.codecool.companymanager.model.mapper.DepartmentMapper;
import com.codecool.companymanager.model.mapper.DepartmentMapperImpl;
import com.codecool.companymanager.service.DepartmentService;
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

import java.util.List;
import java.util.Optional;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService service;

    @MockBean
    private DepartmentMapper departmentMapper;

    @Test
    public void getAll_returnsValidListOfDepartments() throws Exception {
        DepartmentDto department = new DepartmentDto(1L, "Test Department");
        List<DepartmentDto> departments = List.of(department);

        when(departmentMapper.departmentsToDtos(service.findAll())).thenReturn(departments);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(department.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(department.getName())));
    }

    @Test
    public void getById_returnsValidDepartment() throws Exception {
        DepartmentMapper departmentMapper1 = new DepartmentMapperImpl();
        Optional<Department> department = Optional.of(new Department(1L, "Test Department"));
        DepartmentDto departmentDto = departmentMapper1.departmentToDto(department.get());
        when(service.findById(1)).thenReturn(department);
        when(departmentMapper.departmentToDto(department.get())).thenReturn(departmentDto);

        String param = "1";
        mockMvc.perform(get("/api/departments/{id}", param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(departmentDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(departmentDto.getName())));
    }

    @Test
    public void getById_requestForInvalidId_returnsHttpStatusCode404NOT_FOUND() throws Exception {
        String param = "100";
        mockMvc.perform(get("/api/departments/{id}", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("Department with id 100 not found", result.getResponse().getErrorMessage()));
    }

    @Test
    public void addNew_whenDepartmentPosted_thenReturnsWithValidDepartment() throws Exception {
        DepartmentMapper departmentMapper1 = new DepartmentMapperImpl();
        Department department = new Department(1L, "Test Department");
        DepartmentDto departmentDto = departmentMapper1.departmentToDto(department);
        when(departmentMapper.departmentToDto(service.save(department))).thenReturn(departmentDto);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"name\": \"Test Department\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(department.getId().intValue())))
                .andExpect(jsonPath("$.name", is(department.getName())));
    }

    @Test
    public void addNew_withEmptyName_returnsHttpStatusCode400BAD_REQUEST() throws Exception {
        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"name\": \"  \"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}
