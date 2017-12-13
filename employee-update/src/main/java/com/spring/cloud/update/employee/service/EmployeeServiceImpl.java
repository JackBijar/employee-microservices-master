package com.spring.cloud.update.employee.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.cloud.update.employee.model.Employee;
import com.spring.cloud.update.employee.repository.EmployeeRepository;

public class EmployeeServiceImpl implements EmployeeService 
{
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee modelEmployee;
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class);
	
//-------------------------------------------------------------------------------------------------SAVE EMPLOYEE---------

	@Override
	public Employee saveEmployee(Employee employee) 
	{		
		employee.setStatus(true); 						//------SET INACTIVE SING UP STATUS
		employee.setRole("ADMIN_ROLE");					//------SET DEFAULT CANDIDATE ROLE
		
		return employeeRepository.save(employee);
	}

//-----------------------------------------------------------------------------------------------------DELETE EMPLOYEE-------

	@Override
	public int deleteEmployee(long empId) 
	{		
		modelEmployee = employeeRepository.findOne(empId);
		
		if(modelEmployee != null)
		{
			employeeRepository.delete(modelEmployee);	
			
			LOGGER.info("Employee Info Successfully Deleted");
			
			return 1;
		}
		else
		{
			LOGGER.info("Employee Information is NOT Available");			
			return 0;
		}
	}

	@Override
	public Employee updateEmployee(Employee employee) 
	{
		modelEmployee = new Employee();
		
		if(employee.getEmpId() > 0)
		{
			modelEmployee = employeeRepository.findOne(employee.getEmpId());		
			modelEmployee.setEmpId(employee.getEmpId());
		}
		
		if(employee.getName() != null && employee.getName() != "")
			modelEmployee.setName(employee.getName());
		
		if(employee.getEmail() != null && employee.getEmail() != "")
			modelEmployee.setEmail(employee.getEmail());
		
		if(employee.getMobile() != null && employee.getMobile() !="")
			modelEmployee.setMobile(employee.getMobile());
		
		if(employee.getRole() != null && employee.getRole() != "")
			modelEmployee.setRole(employee.getRole());
		
		if(employee.getSalary() > 0)
			modelEmployee.setSalary(employee.getSalary());
		
		if(employee.isStatus() != false)
			modelEmployee.setStatus(employee.isStatus());
		
		return employeeRepository.save(modelEmployee);
	}
}
