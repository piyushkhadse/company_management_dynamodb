package com.stockmarket.company_management.service.impl;

import com.stockmarket.core_d.domain.Error;
import com.stockmarket.core_d.exception.ApplicationException;
import com.stockmarket.core_d.logger.StockMarketApplicationLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Component
public class InternalServiceCall {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${stock.management.deleteStockPrices.uri}")
    private String stockManagementDeleteStockPricesUri;

    StockMarketApplicationLogger logger = StockMarketApplicationLogger.getLogger(this.getClass());

    /**
     * deletes stocks of a company by calling delete stock endpoint
     *
     * @param companyCode
     * @return
     */
    public int deleteStockPricesForCompany(String companyCode, Map<String, String> headers1) {
        final String url = baseUrl + stockManagementDeleteStockPricesUri + "/" + companyCode;
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            HttpHeaders headers = new HttpHeaders();
            String token = headers1.get("authorization").split("Bearer ")[1];
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            return restTemplate.exchange(uri, HttpMethod.DELETE, entity, Object.class).getStatusCodeValue();
        } catch (ApplicationException exception) {
            logger.error().log("Error while deleting stock prices for a companyCode:{}, url:{}", companyCode, url, exception);
            throw exception;
        } catch (Exception e) {
            logger.error().log("Error while deleting stock prices for a companyCode:{}, url:{}", companyCode, url, e);
            throw new ApplicationException(new Error("INTERNAL_SERVER_ERROR", "Internal Server Error"), 500);
        }
    }

}
