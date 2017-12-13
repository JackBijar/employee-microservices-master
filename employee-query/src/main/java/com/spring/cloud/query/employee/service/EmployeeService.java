package com.spring.cloud.query.employee.service;

import java.util.List;

import com.spring.cloud.query.employee.model.Employee;

public interface EmployeeService
{	
	public List<Employee> getEmployees();	
	
	public Employee getEmployee(long empId);
	
	public Employee getEmployeeByEmail(String email);
	
}
