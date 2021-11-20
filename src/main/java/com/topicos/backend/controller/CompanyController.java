package com.topicos.backend.controller;

import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.services.CompanyService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  //CREATE
  @PostMapping("/company/create")
  public CompanyDTO addCompany(@RequestBody CompanyRequestDTO company, @RequestHeader("Authorization") String token) {
    return this.companyService.addCompany(company);
  }

  //DELETE
  @DeleteMapping("/company/delete")
  public void deleteCompany(@RequestParam Long id, @RequestHeader("Authorization") String token) {
    this.companyService.deleteCompany(id);
  }

  //GET
  @GetMapping("/company")
  public List<CompanyDTO> getAllCompanies(@RequestHeader("Authorization") String token) {
    return this.companyService.getAllCompanies();
  }

  //MODIFICATE
  @PutMapping("/company/modify")
  public CompanyDTO modifyCompany(@RequestBody CompanyDTO company, @RequestHeader("Authorization") String token) {
    return this.companyService.modifyCompany(company);
  }

}
