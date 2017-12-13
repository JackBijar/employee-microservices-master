package com.spring.cloud.consumer.employee.repository;

import java.io.IOException;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.cloud.consumer.employee.model.Employee;

@FeignClient(name="employee-query")
public interface RemoteCallService 
{
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST, headers="Accept=application/json")  
	@ResponseBody public Employee saveEmployee(@ModelAttribute ("dataString") Employee employee, BindingResult result);
	
	@RequestMapping(value = "/getEmployees", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<String> getEmployees();
	
	@RequestMapping(value = "/getEmployeeFall", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<String> getEmployeeFall();
	
	@RequestMapping(value = "/getEmployee/{empId}", method = RequestMethod.GET)
	@ResponseBody public ResponseEntity<String> getEmplyee(@PathVariable("empId") long empId);
	
	@RequestMapping(value = "/getEmployeeByEmail/{email}", method = RequestMethod.GET)
	public ResponseEntity<String> getCustomerByName(@PathVariable("email") String email);
	
	@RequestMapping(value = "/updateEmployee/{empId}", method = RequestMethod.PUT, headers="Accept=application/json")  
	@ResponseBody public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("empId") long empId);
	
	@RequestMapping(value = "/deleteEmployee/{empId}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable("empId") long empId);	
}
