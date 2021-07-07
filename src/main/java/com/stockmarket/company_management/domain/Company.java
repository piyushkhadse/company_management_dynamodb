package com.stockmarket.company_management.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.stockmarket.company_management.command.CreateCompany;
import com.stockmarket.core_d.domain.AggregateRoot;
import com.stockmarket.core_d.domain.Error;
import com.stockmarket.core_d.events.CompanyRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "companies")
public class Company extends AggregateRoot implements Comparable{

    private String companyCode;
    private String _id;
    private String companyName;
    private String companyCEO;
    private Double companyTurnover;
    private String companyWebsite;
    private String stockExchange;
    private String createdDate;
    private String createdBy;
    private String modifiedDate;
    private String modifiedBy;

    public Company(CreateCompany createCompany) {
        validate(createCompany);
        if (createCompany.getErrors().isEmpty()) {
            this._id = createCompany.getAggregateId();
            this.companyCode = createCompany.getCompanyCode();
            this.companyName = createCompany.getCompanyName();
            this.companyCEO = createCompany.getCompanyCEO();
            this.companyWebsite = createCompany.getCompanyWebsite();
            this.companyTurnover = createCompany.getCompanyTurnover();
            this.stockExchange = createCompany.getStockExchange();
            Instant createdTempDate = Instant.now();
            this.createdDate = createdTempDate.toString();
            this.modifiedDate = createdTempDate.toString();
            CompanyRegisteredEvent event = new CompanyRegisteredEvent(this._id, createCompany.getAggregateType(),
                    this.companyName, this.companyCode, this.companyCEO, this.companyTurnover, this.companyWebsite,
                    this.stockExchange);
            registerEvent(event);
        }

    }

    private void validate(CreateCompany createCompany) {
        List<Error> errors = new ArrayList<>();
        if (mandatoryCheck(createCompany.getCompanyCode())) {
            errors.add(new Error("INVALID_INPUT", "companyCode is invalid input"));
            createCompany.setErrors(errors);
        } else if (mandatoryCheck(createCompany.getCompanyName())) {
            errors.add(new Error("INVALID_INPUT", "companyName is invalid input"));
            createCompany.setErrors(errors);
        } else if (mandatoryCheck(createCompany.getCompanyCEO())) {
            errors.add(new Error("INVALID_INPUT", "companyCEO is invalid input"));
            createCompany.setErrors(errors);
        } else if (createCompany.getCompanyTurnover() == null) {
            errors.add(new Error("INVALID_INPUT", "companyTurnover is invalid input"));
            createCompany.setErrors(errors);
        } else if (mandatoryCheck(createCompany.getCompanyWebsite())) {
            errors.add(new Error("INVALID_INPUT", "companyWebsite is invalid input"));
            createCompany.setErrors(errors);
        } else if (mandatoryCheck(createCompany.getStockExchange())) {
            errors.add(new Error("INVALID_INPUT", "stockExchange is invalid input"));
            createCompany.setErrors(errors);
        } else if (createCompany.getCompanyTurnover().compareTo(10.00d) < 0) {
            errors.add(new Error("INVALID_INPUT", "companyTurnover must be greater than 10Cr."));
            createCompany.setErrors(errors);
        }
    }

    private boolean mandatoryCheck(String field) {
        if (field == null || field.isEmpty()) {
            return true;
        }
        return false;
    }

    @DynamoDBHashKey
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    @DynamoDBAttribute
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @DynamoDBAttribute
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @DynamoDBAttribute
    public String getCompanyCEO() {
        return companyCEO;
    }

    public void setCompanyCEO(String companyCEO) {
        this.companyCEO = companyCEO;
    }

    @DynamoDBAttribute
    public Double getCompanyTurnover() {
        return companyTurnover;
    }

    public void setCompanyTurnover(Double companyTurnover) {
        this.companyTurnover = companyTurnover;
    }

    @DynamoDBAttribute
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    @DynamoDBAttribute
    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    @DynamoDBAttribute
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @DynamoDBAttribute
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @DynamoDBAttribute
    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @DynamoDBAttribute
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
