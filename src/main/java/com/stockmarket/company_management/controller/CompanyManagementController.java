package com.stockmarket.company_management.controller;

import com.stockmarket.company_management.command.CreateCompany;
import com.stockmarket.company_management.domain.Company;
import com.stockmarket.company_management.projection.CompanyProjection;
import com.stockmarket.company_management.projection.view.CompanyView;
import com.stockmarket.company_management.service.CompanyManagementService;
import com.stockmarket.core_d.logger.StockMarketApplicationLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CompanyManagementController {

    private static final String COMPANY_V1_0 = "/v1.0/market/company";

    StockMarketApplicationLogger logger = StockMarketApplicationLogger.getLogger(this.getClass());

    @Autowired
    private CompanyManagementService companyManagementService;

    @Autowired
    private CompanyProjection projection;

    @GetMapping(value = COMPANY_V1_0 + "/{company_code}")
    public ResponseEntity<CompanyView> getCompanyDetails(@PathVariable String company_code) {
        logger.info().log("Inside getCompanyDetails()");
        CompanyView view = projection.getCompanyDetails(company_code);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }


    @GetMapping(value = COMPANY_V1_0)
    public ResponseEntity<List<CompanyView>> getAllCompanyDetails() {
        logger.info().log("Inside getAllCompanyDetails()");
        return new ResponseEntity<>(projection.getAllCompanyDetails(), HttpStatus.OK);
    }

    @PostMapping(value = COMPANY_V1_0 + "/register")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<Company> doCompanyRegistration(@RequestBody CreateCompany createCompany) {
        logger.info().log("Inside company registration");
        return new ResponseEntity<>(companyManagementService.companyRegistration(createCompany), HttpStatus.CREATED);
    }

    @DeleteMapping(value = COMPANY_V1_0 + "/delete/{company_code}")
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseEntity<Void> deleteCompany(@PathVariable String company_code, @RequestHeader Map<String, String> headers) {
        logger.info().log("Inside deleteCompany()");
        companyManagementService.deleteCompany(company_code,headers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
