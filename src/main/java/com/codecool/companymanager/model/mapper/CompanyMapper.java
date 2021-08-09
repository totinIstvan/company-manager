package com.codecool.companymanager.model.mapper;

import com.codecool.companymanager.model.dto.CompanyDto;
import com.codecool.companymanager.model.entity.Company;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDto companyToDto(Company company);

    List<CompanyDto> companiesToDtos(List<Company> companies);

    @Mapping(target = "departments", ignore = true)
    @Named("summary")
    CompanyDto companyToSummaryDto(Company company);

    Company companyDtoToCompany(CompanyDto companyDto);

    @IterableMapping(qualifiedByName = "summary")
    List<CompanyDto> companiesToSummaryDtos(List<Company> companies);
}
