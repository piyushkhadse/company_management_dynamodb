package com.stockmarket.company_management.service;

import com.stockmarket.company_management.command.CreateCompany;
import com.stockmarket.company_management.domain.Company;

import java.util.Map;

public interface CompanyManagementService {
    Company companyRegistration(CreateCompany createCompany);

    void deleteCompany(String companyCode, Map<String, String> headers);
}
