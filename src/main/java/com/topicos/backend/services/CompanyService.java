package com.topicos.backend.services;

import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.utils.Mappers;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  public List<CompanyDTO> getAllCompanies() {
    return this.companyRepository
        .findAll()
        .stream()
        .map(Mappers::buildCompanyDTO)
        .collect(Collectors.toList());
  }

  public CompanyDTO addCompany(CompanyRequestDTO companyRequest) {
    Company company = this.companyRepository.save(Mappers.buildCompany(companyRequest));
    return Mappers.buildCompanyDTO(company);
  }

  public void deleteCompany(Long companyId) {
    Optional<Company> company = this.companyRepository.findById(companyId);
    company.ifPresent(this.companyRepository::delete);
  }


  public CompanyDTO modifyCompany(CompanyDTO company) {
    Optional<Company> optionalCompany = this.companyRepository.findById(company.getId());
    if (optionalCompany.isPresent()) {
      Company com = optionalCompany.get();
      com.setName(company.getName());
      com.setBusinessArea(company.getBusinessArea());
      com.setRut(company.getRut());
      com.setBusinessName(company.getBusinessName());
      this.companyRepository.save(com);
      return Mappers.buildCompanyDTO(com);
    }
    return null;
  }


}
