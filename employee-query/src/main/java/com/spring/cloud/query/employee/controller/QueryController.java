package com.spring.cloud.query.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.spring.cloud.query.employee.model.Employee;
import com.spring.cloud.query.employee.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value="employee")
@Api(value="queryController", description="Operations pertaining to products in queries about employees")
public class QueryController 
{
	@Autowired
	private EmployeeService employeeService; 
	
	private Employee modelEmployee;
	
	private static final Logger LOGGER = Logger.getLogger(QueryController.class);	

//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE--------------	
	@ApiOperation(value = "View a list of available employees",response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	@RequestMapping(value = "/getEmployees", method = RequestMethod.GET, headers="Accept=application/json", produces = "application/json")
	public List<Employee> getEmployees()
	{	
		List<Employee> employeeList = new ArrayList<>();
		
		employeeList = employeeService.getEmployees();
		
		System.out.println("IN Employee-Query => getEmployees");
		
		if(employeeList.isEmpty())
			LOGGER.info("EMPLOYEES ARE NOT AVAILABLE");
		else
			return employeeList ;
		
		return null;
	}
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE--------------	
	@ApiOperation(value = "Get Employee Fall Back Method 1 execuation")
	@RequestMapping(value = "/getEmployeeFall-1", method = RequestMethod.GET, headers="Accept=application/json", produces = "application/json")
	@HystrixCommand(fallbackMethod = "getEmployeeFallbackMethod")
	public Employee getEmployeeFallback()
	{
		Employee employee = null;
		
		if(employee  == null)
			throw new RuntimeException();
		
		return employee;
	}	
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE FALL BACK METHOD 1--------------

	public Employee getEmployeeFallbackMethod() 
	{
		Employee emp = new Employee();
		
		LOGGER.info("IN Employee Fallback Method");
		
		emp.setEmpId(011);
		emp.setName("Default Name : RAJIB GARAI");
		emp.setEmail("Default Email : rajib@gmail.com");
		emp.setMobile("Default Mobile : 9563359659");
		emp.setSalary(50000);

		return emp;
	}
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE--------------	
	
	/*@HystrixCommand(fallbackMethod = "getEmployeeFall2", commandProperties={
	@HystrixProperty(name="circuitBreaker.sleepWindowInMillisecond", value="60000"),
	@HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="5")})*/

	@RequestMapping(value = "/getEmployeeFall-2", method = RequestMethod.GET, headers="Accept=application/json", produces = "application/json")
	@HystrixCommand(fallbackMethod = "getEmployeeFallbackMethod2")	
	public Employee getEmployeeFallback2()
	{
		Employee employee = null;
		
		if(employee  == null)
			throw new RuntimeException();
		
		employee = new Employee();
			
		employee.setEmpId(011);
		employee.setName("RAJIB GARAI !");
		employee.setEmail("rajib@gmail.com !");
		employee.setMobile("9563359659 !");
		employee.setSalary(50000);
		
		return employee;
	}
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE FALL BACK METHOD 2--------------

	public Employee getEmployeeFallbackMethod2() 
	{
		Employee emp = new Employee();
		
		LOGGER.info("IN Employee Fallback-2");
		
		emp.setEmpId(011);
		emp.setName("Default Name 2 : RAJIB GARAI");
		emp.setEmail("Default Email 2 : rajib@gmail.com");
		emp.setMobile("Default Mobile 2 : 9563359659");
		emp.setSalary(55500);

		return emp;
	}
	
//---------------------------------------------------------------------------------------------------------GET EMPLOYEE BY ID---------------------
	@ApiOperation(value = "Search a employee with an ID",response = Employee.class)
	@RequestMapping(value = "/getEmployee/{empId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody public Employee getEmplyee(@PathVariable("empId") long empId) 
	{
		LOGGER.info("GET EMPLOYEE BY ID "+empId);
		
		if(empId > 0)
		{
			modelEmployee = employeeService.getEmployee(empId);
			
			if(modelEmployee.getEmpId() > 0)
				return modelEmployee;
			else
				LOGGER.info("EMPLOYEE IS NOT EXIST");
		}
		else
			LOGGER.info("EMPLOYEE IS NULL");
		
		return null;
	}	
	
//-------------------------------------------------------------------------------------------------------GET EMPLOYEE BY EMAIL-------
	@ApiOperation(value = "Search a employee with an Email",response = Employee.class)
	@RequestMapping(value = "/getEmployeeByEmail/{email}", method = RequestMethod.GET, produces = "application/json")
	public Employee getCustomerByName(@PathVariable("email") String email) 
	{
		LOGGER.info("In Get Employee By Email Controller :: "+email);
		
		if(email != null)
		{
			modelEmployee = employeeService.getEmployeeByEmail(email);
			
			if(modelEmployee.getEmpId() > 0)
				return modelEmployee;
			else
				LOGGER.info("THESE EMPLOYEE IS NOT EXIST");
		}
		else
			LOGGER.info("RECEIVED EMAIL ID IS NULL");
		
		return null;
	}	
	
		

}
