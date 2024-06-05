package com.fallback.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalController {

	@Autowired
	private ExternalService externalService;
	
	@GetMapping("/app/{id}")
	public String getEmployee(@PathVariable Long id) {
		System.out.println("Received request for employee ID: " + id);
		return externalService.getEmployeeById(id);
		//return "Hello welcome" ;
	}
}
