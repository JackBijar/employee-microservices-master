package com.spring.cloud.update.employee.service;

import java.util.List;

import com.spring.cloud.update.employee.model.Employee;

public interface EmployeeService
{
	public Employee saveEmployee(Employee employee);	
			
	public int deleteEmployee(long empId);
	
	public Employee updateEmployee(Employee employee);
	
}
