package com.codecool.companymanager.model.mapper;

import com.codecool.companymanager.model.dto.EmployeeDto;
import com.codecool.companymanager.model.entity.Employee;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto employeeToDto(Employee employee);

    List<EmployeeDto> employeesToDtos(List<Employee> employees);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Named("summary")
    EmployeeDto employeeToSummaryDto(Employee employee);

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);

    @IterableMapping(qualifiedByName = "summary")
    List<EmployeeDto> employeesToSummaryDtos(List<Employee> employees);
}
