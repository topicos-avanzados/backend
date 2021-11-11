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

  public List<CompanyDTO> getAllCompanys() {
    return this.companyRepository
        .findAll()
        .stream()
        .map(Mappers::buildCompanyDTO)
        .collect(Collectors.toList());
  }

  public CompanyDTO addCompany(CompanyRequestDTO companyRequest) {
    Company company = this.companyRepository.save(Company
        .builder()
        .name(companyRequest.getName())
        .build());
    return CompanyDTO
        .builder()
        .id(company.getId())
        .name(company.getName())
        .build();
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
      this.companyRepository.save(com);
      return Mappers.buildCompanyDTO(com);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }


}
