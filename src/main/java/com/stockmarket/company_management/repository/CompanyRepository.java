package com.stockmarket.company_management.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.stockmarket.company_management.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;


    public Company save(Company company) {
        dynamoDBMapper.save(company);
        return company;
    }

    public Company findByCompanyCode(String companyCode) {
        return dynamoDBMapper.load(Company.class, companyCode);
    }

    public List<Company> findAll() {
        List<Company> resp = new ArrayList<>();
        try {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            resp = dynamoDBMapper.scan(Company.class, scanExpression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public void deleteByCompanyCode(Company company) {
        dynamoDBMapper.delete(company);
    }

}
