package com.spring.cloud.consumer.employee.repository;

import org.springframework.data.repository.CrudRepository;

import com.spring.cloud.consumer.employee.model.Employee;


public interface EmployeeRepository extends CrudRepository<Employee, Long>
{

}
