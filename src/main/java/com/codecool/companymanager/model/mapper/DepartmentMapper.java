package com.codecool.companymanager.model.mapper;

import com.codecool.companymanager.model.dto.DepartmentDto;
import com.codecool.companymanager.model.entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    List<DepartmentDto> departmentsToDtos(List<Department> departments);

    DepartmentDto departmentToDto(Department department);

    Department departmentDtoToDepartment(DepartmentDto departmentDto);

    List<Department> departmentDtosToDepartments(List<DepartmentDto> departments);
}
