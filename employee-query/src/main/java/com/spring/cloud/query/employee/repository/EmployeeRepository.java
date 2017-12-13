package com.spring.cloud.query.employee.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.cloud.query.employee.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>
{
	public Employee findByEmail(String email);
}
