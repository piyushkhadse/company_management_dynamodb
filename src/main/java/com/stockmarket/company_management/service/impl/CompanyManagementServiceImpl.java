package com.stockmarket.company_management.service.impl;

import com.stockmarket.company_management.command.CreateCompany;
import com.stockmarket.company_management.domain.Company;
import com.stockmarket.company_management.repository.CompanyRepository;
import com.stockmarket.company_management.service.CompanyManagementService;
import com.stockmarket.core_d.domain.Error;
import com.stockmarket.core_d.events.CompanyDeletedEvent;
import com.stockmarket.core_d.events.publisher.KafkaPublisher;
import com.stockmarket.core_d.exception.ApplicationException;
import com.stockmarket.core_d.logger.StockMarketApplicationLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class CompanyManagementServiceImpl implements CompanyManagementService {
    @Autowired
    private KafkaPublisher publisher;

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private InternalServiceCall internalServiceCall;


    StockMarketApplicationLogger logger = StockMarketApplicationLogger.getLogger(this.getClass());

    /**
     * registers a company
     *
     * @param createCompany
     * @return
     */
    @Override
    public Company companyRegistration(CreateCompany createCompany) {
        try {
            Company company = new Company(createCompany);
            if (!createCompany.getErrors().isEmpty()) {
                throw new ApplicationException(createCompany.getErrors().get(0), 400);
            }
            Company savedCompany = repository.save(company);
            publisher.publish(company.events().get(0));
            return savedCompany;
        } catch (ApplicationException exception) {
            logger.error().log("Error while registering company", exception);
            throw exception;
        } catch (Exception e) {
            logger.error().log("Error while registering company", e);
            throw new ApplicationException(new Error("INTERNAL_SERVER_ERROR", "Internal Server Error"), 500);
        }
    }

    /**
     * deletes a company and its related stocks
     *
     * @param companyCode
     */
    @Override
    @Transactional
    public void deleteCompany(String companyCode, Map<String, String> headers) {
        try {
            Company company = repository.findByCompanyCode(companyCode);
            if (company != null) {
                CompanyDeletedEvent event = new CompanyDeletedEvent(
                        company.get_id(),
                        "COMPANY",
                        company.getCompanyName(),
                        company.getCompanyCode(),
                        company.getCompanyCEO(),
                        company.getCompanyTurnover(),
                        company.getCompanyWebsite(),
                        company.getStockExchange()
                );
                repository.deleteByCompanyCode(company);
                publisher.publish(event);
                int statusCode = internalServiceCall.deleteStockPricesForCompany(companyCode, headers);
                logger.info().log("response of deleting stock prices for companyCode statusCode:{}", statusCode);
            } else {
                throw new ApplicationException(new Error("NOT_FOUND", "Company code is not found"), 404);
            }
        } catch (ApplicationException exception) {
            logger.error().log("Exception while deleting company for company code {}", companyCode, exception);
            throw exception;
        } catch (Exception e) {
            logger.error().log("Exception while deleting company for company code {}", companyCode, e);
            throw new ApplicationException(new Error("INTERNAL_SERVER_ERROR", "Internal Server Error"), 500);
        }
    }
}
