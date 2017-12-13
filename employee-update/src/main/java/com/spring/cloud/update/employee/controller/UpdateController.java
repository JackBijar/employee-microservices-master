package com.spring.cloud.update.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.spring.cloud.update.employee.model.Employee;
import com.spring.cloud.update.employee.service.EmployeeService;

@RestController
@RequestMapping(value="employee")
public class UpdateController 
{
	@Autowired
	private EmployeeService employeeService; 
	
	private Employee modelEmployee;
	
	private static final Logger LOGGER = Logger.getLogger(UpdateController.class);
	
//-------------------------------------------------------------------------------------------------------SAVE EMPLOYEE INFO----------------

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)  
	 @ResponseBody public Employee saveEmployee(@RequestBody Employee employee, BindingResult result)  
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+employee);
		
		if(employee.getEmail() != null || employee.getEmail() != "")
			{				
				if(modelEmployee == null)
				{
					modelEmployee = employeeService.saveEmployee(employee);	
					
					if(modelEmployee.getEmpId() > 0)
					{
						LOGGER.info("EMPLOYEE SUCCESSFULLY SAVE");
						return modelEmployee;
					}
					else
						LOGGER.info("EMPLOYEE NOT SUCCESSFULLY SAVE");	
				}
				else				
					LOGGER.info("EMPLOYEE ALREADY EXIST");
			}
			else
				LOGGER.info("EMPLOYEE EMAIL IS NULL");
		
		return null;
	}
	
//--------------------------------------------------------------------------------------------------------------DELETE EMPLOYEE-------------

	@RequestMapping(value = "/deleteEmployee/{empId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteEmployee(@PathVariable("empId") long empId) 
	{
		LOGGER.info("IN DELETE EMPLOYEE CONTROLLER"+empId);
		
		if(empId > 0)
		{
			int i = employeeService.deleteEmployee(empId);			
			
			if(i > 0)
				LOGGER.info("EMPLOYEE SUCCESSFULLY DELETED");
			else
				LOGGER.info("EMPLOYEE NOT SUCCESSFULLY DELETED");
		}
		else
			LOGGER.info("RECEIVED empId IS NULL");
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}	
	
//--------------------------------------------------------------------------------------------------------------DELETE EMPLOYEE-------------
	
	@RequestMapping(value = "/deleteEmployeeFall-1/{empId}", method = RequestMethod.DELETE)
	//@HystrixCommand(fallbackMethod = "deleteEmployeeFall1")	
	@HystrixCommand(fallbackMethod = "deleteEmployeeFall1", commandProperties={
	@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="60000"),
	@HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="5")})
	public ResponseEntity<String> deleteEmployeeFall(@PathVariable("empId") long empId) 
	{
		LOGGER.info("IN DELETE EMPLOYEE FALL BACK - 1 CONTROLLER : "+empId);
		
		if(empId > 0)
			throw new RuntimeException();
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE FALL BACK METHOD 2--------------

	public ResponseEntity<String> deleteEmployeeFall1(@PathVariable("empId") long empId) 
	{
		LOGGER.info("<<========== FALL BACK ========>>");
		
		LOGGER.info("IN DELETE EMPLOYEE FALL BACK - 1 CONTROLLER : "+empId);			
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}	
//--------------------------------------------------------------------------------------------------------UPDATE EMPLOYEE----------------------
	
	@RequestMapping(value = "/updateEmployee/{empId}", method = RequestMethod.PUT, headers="Accept=application/json")  
	 @ResponseBody public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("empId") long empId)  
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+employee);		
		
		if(employee != null)
		{
			if(empId > 0)	
			{			
				modelEmployee = employeeService.updateEmployee(employee);
				
				if(modelEmployee.getEmpId() > 0)
					return modelEmployee;
				else
					LOGGER.info("EMPLOYEE NOT SUCCESSFULLY UPDATED");
			}		
			else
			{
				modelEmployee = employeeService.saveEmployee(employee);
				
				if(modelEmployee.getEmpId() > 0)
					return modelEmployee;
				else
					LOGGER.info("NEW EMPLOYEE NOT SUCCESSFULLY SAVE");
			}
		}
		else
			LOGGER.info("RECEIVED EMPLOYEE IS NULL");
		
		return null;
	}	

}
