package com.topicos.backend.controller;

import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
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

  private final JwtTokenUtil jwtTokenUtil;

  //CREATE
  @PostMapping("/company/create")
  public CompanyDTO addCompany(@RequestBody CompanyRequestDTO company, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.companyService.addCompany(company);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

  //DELETE
  @DeleteMapping("/company/delete")
  public void deleteCompany(@RequestParam Long id, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      this.companyService.deleteCompany(id);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

  //GET
  @GetMapping("/company")
  public List<CompanyDTO> getAllCompanies(@RequestHeader("Authorization") String token) {
    return this.companyService.getAllCompanies();
  }

  //MODIFICATION
  @PutMapping("/company/modify")
  public CompanyDTO modifyCompany(@RequestBody CompanyDTO company, @RequestHeader("Authorization") String token) {
    return this.companyService.modifyCompany(company);
  }

}
