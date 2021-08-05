package com.codecool.companymanager.model.mapper;

import com.codecool.companymanager.model.dto.EmployeeDto;
import com.codecool.companymanager.model.entity.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    List<EmployeeDto> employeesToDtos(List<Employee> employees);

    EmployeeDto employeeToDto(Employee employee);

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);

    List<Employee> employeeDtosToEmployees(List<EmployeeDto> employees);
}
