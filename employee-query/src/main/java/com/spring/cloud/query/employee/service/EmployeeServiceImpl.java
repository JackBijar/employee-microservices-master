package com.spring.cloud.query.employee.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.cloud.query.employee.model.Employee;
import com.spring.cloud.query.employee.repository.EmployeeRepository;

public class EmployeeServiceImpl implements EmployeeService 
{
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee modelEmployee;
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class);
	
//---------------------------------------------------------------------------------------------------GET ALL EMPLOYEES-----
	
	@Override
	public List<Employee> getEmployees() 
	{		
		return (List<Employee>) employeeRepository.findAll();
	}
	
//----------------------------------------------------------------------------------------------------SEARCH EMPLOYEE EMP ID------

	@Override
	public Employee getEmployee(long empId) 
	{
		return employeeRepository.findOne(empId);
	}
	
//----------------------------------------------------------------------------------------------------SEARCH EMPLOYEE BY EMAIL----

	@Override
	public Employee getEmployeeByEmail(String email)
	{		
		return employeeRepository.findByEmail(email);
	}
}
