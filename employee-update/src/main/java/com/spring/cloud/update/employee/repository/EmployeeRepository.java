package com.spring.cloud.update.employee.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.cloud.update.employee.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>
{
	
}
