package com.fallback.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ExternalService {

	 private static final String EMPLOYEE_SERVICE = "employeeService";

	    @Autowired
	    private RestTemplate restTemplate;

	    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "defaultResponse")
	    
	    public String getEmployeeById(Long id) {
	    	
	        String url = "http://localhost:9090/api/v1/employees/" + id;
	        System.out.println("Calling external service: " + url);
	        try {
	            return restTemplate.getForObject(url, String.class);
	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode().is4xxClientError()) {
	                return "Custom message: Employee ID " + id + " not found";
	            }
	            throw e; // Rethrow the exception if it's not a client error
	        } catch (ResourceAccessException e) {
	            throw new RuntimeException("Service is unavailable", e); // Rethrow as RuntimeException to trigger fallback
	        }
	    }

	    public String defaultResponse(Long id, Throwable t) {
	        System.out.println("Fallback triggered due to: " + t.getMessage());
	        return "Fallback response: Employee service is unavailable";
	    }
}
