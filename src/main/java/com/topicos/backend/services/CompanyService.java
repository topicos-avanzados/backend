package com.topicos.backend.services;

import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.security.JwtTokenUtil;
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

  private final JwtTokenUtil jwtTokenUtil;

  private final LogService logService;

  public List<CompanyDTO> getAllCompanies() {
    return this.companyRepository
        .findAll()
        .stream()
        .map(Mappers::buildCompanyDTO)
        .collect(Collectors.toList());
  }

  public CompanyDTO addCompany(CompanyRequestDTO companyRequest, String token) {
    Company company = this.companyRepository.save(Mappers.buildCompany(companyRequest));

    LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se registro una nueva empresa: " + company.getName());
    logService.addLog(newLog);

    return Mappers.buildCompanyDTO(company);
  }

  public void deleteCompany(Long companyId, String token) {
    Optional<Company> company = this.companyRepository.findById(companyId);
    if(company.isPresent()){
      this.companyRepository.delete(company.get());
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se elimino la empresa: " + company.get().getName());
      logService.addLog(newLog);
    }
  }

  public CompanyDTO modifyCompany(CompanyDTO company, String token) {
    Optional<Company> optionalCompany = this.companyRepository.findById(company.getId());
    if (optionalCompany.isPresent()) {
      Company com = optionalCompany.get();
      com.setName(company.getName());
      com.setBusinessArea(company.getBusinessArea());
      com.setRut(company.getRut());
      com.setBusinessName(company.getBusinessName());
      this.companyRepository.save(com);
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se actualizo la empresa: " + optionalCompany.get().getName());
      logService.addLog(newLog);
      return Mappers.buildCompanyDTO(com);
    }
    return null;
  }


}
