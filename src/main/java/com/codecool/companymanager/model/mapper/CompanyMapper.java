package com.codecool.companymanager.model.mapper;

import com.codecool.companymanager.model.dto.CompanyDto;
import com.codecool.companymanager.model.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    List<CompanyDto> companiesToDtos(List<Company> companies);

    CompanyDto companyToDto(Company company);

    Company companyDtoToCompany(CompanyDto companyDto);

}
