package com.spring.cloud.consumer.employee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.spring.cloud.consumer.employee.model.Employee;
import com.spring.cloud.consumer.employee.security.JwtAuthenticationRequest;
import com.spring.cloud.consumer.employee.security.JwtAuthenticationResponse;


@RestController
@RequestMapping(value="employee")
public class ConsumerController 
{
	//@Autowired
	//private DiscoveryClient discoveryClient;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	//@Autowired
	//private LoadBalancerClient loadBalancerClient;
	
	private Employee modelEmployee;
	
	private static final Logger LOGGER = Logger.getLogger(ConsumerController.class);
	
	private String baseURI = "";
	private String employeeUpdateUrl = "";
	private String employeeQueryUrl = "";
	
	private String deviceName = "";
	
	
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> response = null;	
	
//--------------------------------------------------------------------------------------------------------RESPONSE HEADER--------------
	
	private static HttpEntity<?> getHeaders() throws IOException 
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
	
//-------------------------------------------------------------------------------------------------------Login EMPLOYEE----------------

	@RequestMapping(value = "/auth", method = RequestMethod.POST, headers="Accept=application/json")  
	 @ResponseBody public ResponseEntity<String> logIn(@ModelAttribute ("dataString") JwtAuthenticationRequest authenticationRequest, Device device, BindingResult result) throws RestClientException, IOException
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+authenticationRequest);			
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
				
		employeeUpdateUrl = baseURI+"/auth/signIn";		
		
		try
		{				
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("username", authenticationRequest.getUsername());
			map.add("password", authenticationRequest.getPassword());				

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			response = restTemplate.postForEntity(employeeUpdateUrl, request , String.class);
			
			System.out.println("TOKEN => "+response);
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}			
		
		return response;
	}
	
//-------------------------------------------------------------------------------------------------------SIGN UP EMPLOYEE----------------

	@RequestMapping(value = "/signUp", method = RequestMethod.POST, headers="Accept=application/json")  
	 @ResponseBody public ResponseEntity<String> signUp(@ModelAttribute ("dataString") JwtAuthenticationRequest authenticationRequest, BindingResult result) throws RestClientException, IOException
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+authenticationRequest);			
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
				
		employeeUpdateUrl = baseURI+"/auth/signUp";		
		
		try
		{				
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("firstname", authenticationRequest.getFirstname());
			map.add("lastname", authenticationRequest.getLastname());
			map.add("email", authenticationRequest.getEmail());
			map.add("password", authenticationRequest.getPassword());				

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			response = restTemplate.postForEntity(employeeUpdateUrl, request , String.class);
			
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}			
		
		return response;
	}	
	
//-------------------------------------------------------------------------------------------------------SAVE EMPLOYEE INFO----------------

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST, headers="Accept=application/json")  
	 @ResponseBody public Employee saveEmployee(@ModelAttribute ("dataString") Employee employee, BindingResult result) throws RestClientException, IOException
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+employee);
		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-update");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-update");		//----> After Zuul API Gateway
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		//employeeUpdateUrl = baseURI+"/employee/saveEmployee";							//----> After Zuul API Gateway
		
		employeeUpdateUrl = baseURI+"/employee-update/employee/saveEmployee";
		
		
		if(employee.getEmail() != null || employee.getEmail() != "")
			{
				try
				{							 
					modelEmployee = restTemplate.postForObject(employeeUpdateUrl, employee, Employee.class);						
				}
				catch (Exception ex)
				{
					System.out.println(ex);
				}
			}
			else
				LOGGER.info("EMPLOYEE EMAIL IS NULL");
		
		return modelEmployee;
	}
		
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE--------------	

	@RequestMapping(value = "/getEmployees", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<String> getEmployees(HttpServletRequest request) throws RestClientException, IOException
	{		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-query");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		String token = request.getHeader("Authorization");
		
		System.out.println("IN Employee-Consumer => getEmployees");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		//employeeQueryUrl = baseURI+"/employee/getEmployees";
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployees";
		
		System.out.println(employeeQueryUrl);
				
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			headers.add("Authorization", token);
			
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	        response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, entity, String.class);
	        
			
			//response = restTemplate.postForEntity(employeeUpdateUrl, request , String.class);
						
			//response = restTemplate.getForEntity(employeeUpdateUrl, String.class, headers);
			
			//response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, headers, String.class);
			
			System.out.println("=========>>>> >>> "+response);
			
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}		
		return response;
	}
	
//--------------------------------------------------------------------------------------------------------SHOW ALL EMPLOYEE FALLBACK--------------	

	@RequestMapping(value = "/getEmployeeFall", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<String> getEmployeeFall() throws RestClientException, IOException
	{		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-query");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployeeFall";
		
		System.out.println(employeeQueryUrl);
				
		try
		{
			response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, getHeaders(), String.class);			
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}		
		return response;
	}
	
//---------------------------------------------------------------------------------------------------------GET EMPLOYEE BY ID---------------------

	@RequestMapping(value = "/getEmployee/{empId}", method = RequestMethod.GET)	
	@ResponseBody public ResponseEntity<String> getEmplyee(@PathVariable("empId") long empId) throws RestClientException, IOException
	{
		LOGGER.info("GET EMPLOYEE BY ID "+empId);
		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-query");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployee/"+empId;
		
		if(empId > 0)
		{
			try
			{
				response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, getHeaders(), String.class);			
			}
			catch (Exception ex)
			{
				System.out.println(ex);
			}	
		}
		else
			LOGGER.info("EMPLOYEE IS NULL");
		
		return response;
	}	
	
//---------------------------------------------------------------------------------------------------------GET EMPLOYEE BY ID Fallback---------------------
	
	@RequestMapping(value = "/getEmployeeFall/{empId}", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "fallbackMethodInConsumer")
	@ResponseBody public ResponseEntity<String> getEmplyeeFall(@PathVariable("empId") long empId) throws RestClientException, IOException
	{
		LOGGER.info("GET EMPLOYEE BY ID "+empId);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployee/"+empId;
		
		if(empId > 0)
		{
			try
			{
				response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, getHeaders(), String.class);			
			}
			catch (Exception ex)
			{
				System.out.println(ex);
			}	
			
			throw new RuntimeException();
		}
		else
			LOGGER.info("EMPLOYEE IS NULL");
		
		return response;
	}	
																				//--------------------------FALL BACK METHOD------------------
	
	@ResponseBody public ResponseEntity<String> fallbackMethodInConsumer(@PathVariable("empId") long empId) 
	{
		LOGGER.info("IN GET EMPLOYEE BY ID FALL BACK "+empId);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployee/"+empId;
		try
		{
			response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, getHeaders(), String.class);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}
	
//-------------------------------------------------------------------------------------------------------GET EMPLOYEE BY EMAIL-------

	@RequestMapping(value = "/getEmployeeByEmail/{email}", method = RequestMethod.GET)
	public ResponseEntity<String> getCustomerByName(@PathVariable("email") String email) throws RestClientException, IOException
	{
		LOGGER.info("In Get Employee By Email Controller :: "+email);
		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-query");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-query");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeQueryUrl = baseURI+"/employee-query/employee/getEmployeeByEmail/"+email;
		
		if(email != null)
		{
			try
			{
				response = restTemplate.exchange(employeeQueryUrl, HttpMethod.GET, getHeaders(), String.class);			
			}
			catch (Exception ex)
			{
				System.out.println(ex);
			}	
		}
		else
			LOGGER.info("RECEIVED EMAIL ID IS NULL");
		
		return response;
	}
	
//--------------------------------------------------------------------------------------------------------UPDATE EMPLOYEE----------------------

	@RequestMapping(value = "/updateEmployee/{empId}", method = RequestMethod.PUT, headers="Accept=application/json")  
	 @ResponseBody public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("empId") long empId)  throws RestClientException, IOException
	 {	
		LOGGER.info("RECIVE EMPLOYEE INFO : "+employee);	
		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-update");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-update");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeUpdateUrl = baseURI+"/employee-update/employee/updateEmployee/"+empId;
		
		if(employee != null)
		{
			if(empId > 0)	
			{			
				try
				{							 
					modelEmployee = restTemplate.postForObject( employeeUpdateUrl, employee, Employee.class);						
				}
				catch (Exception ex)
				{
					System.out.println(ex);
				}				
			}		
			else
			{
				/*try
				{							 
					modelEmployee = restTemplate.postForObject( employeeUpdateUrl+"/saveEmployee", employee, Employee.class);						
				}
				catch (Exception ex)
				{
					System.out.println(ex);
				}	*/		
				
				LOGGER.info("EMPLOYEE IS NOT AVAILABLE");
			}
		}
		else
			LOGGER.info("RECEIVED EMPLOYEE IS NULL");
		
		return modelEmployee;
	}
	
//--------------------------------------------------------------------------------------------------------------DELETE EMPLOYEE-------------

	@RequestMapping(value = "/deleteEmployee/{empId}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable("empId") long empId) throws RestClientException, IOException
	{
		LOGGER.info("IN DELETE EMPLOYEE CONTROLLER ==> "+empId);
		
		//List<ServiceInstance> instances = discoveryClient.getInstances("employee-update");
		//ServiceInstance serviceInstance = instances.get(0);
		
		//ServiceInstance serviceInstance=loadBalancer.choose("employee-update");
		
		ServiceInstance serviceInstance=loadBalancer.choose("gateway-service");
		
		baseURI = serviceInstance.getUri().toString();
		
		employeeUpdateUrl = baseURI+"/employee-update/employee/deleteEmployee";
		
		if(empId > 0)
		{
			Map<String, String> params = new HashMap<String, String>();
		    params.put("empId", ""+empId);
		    
			restTemplate.delete(employeeUpdateUrl, params);			
		}
		else
			LOGGER.info("RECEIVED empId IS NULL");
	}

//==============================================================================================REST SERVICE METHODS=======

	@RequestMapping(value = "/testHeadMethod", method = RequestMethod.HEAD)
	@ResponseBody public Employee testHeadMethod() throws RestClientException, IOException
	{
		LOGGER.info("IN TEST HEAD METHOD CONTROLLER");
		
		Employee emp = new Employee();
		
		emp.setEmpId(021);
		emp.setName("Test-HEAD-METHOD Name : RAJIB GARAI");
		emp.setEmail("Test-HEAD-METHOD Email : rajib@gmail.com");
		emp.setMobile("Test-HEAD-METHOD Mobile : 9563359659");
		emp.setSalary(110000);
		
		return emp;
	}																				
	
//--------------------------------------------------------------------------------------------------------------OPTION METHOD-------------
	
	@RequestMapping(value = "/testOptionMethod", method = RequestMethod.OPTIONS)
	@ResponseBody public Employee testOptionMethod() throws RestClientException, IOException
	{
		LOGGER.info("IN TEST OPTION METHOD CONTROLLER");
		
		Employee emp = new Employee();
		
		emp.setEmpId(021);
		emp.setName("Test-1 Name : RAJIB GARAI");
		emp.setEmail("Test-1 Email : rajib@gmail.com");
		emp.setMobile("Test-1 Mobile : 9563359659");
		emp.setSalary(110000);
		
		return emp;
	}	
	
//--------------------------------------------------------------------------------------------------------------PATCH METHOD-------------

	@RequestMapping(value = "/testPatchMethod", method = RequestMethod.PATCH)
	@ResponseBody public Employee testPatchMethod() throws RestClientException, IOException
	{
		LOGGER.info("IN TEST PATCH METHOD CONTROLLER");
		
		Employee emp = new Employee();
		
		emp.setEmpId(021);
		emp.setName("Test-1 Name : RAJIB GARAI");
		emp.setEmail("Test-1 Email : rajib@gmail.com");
		emp.setMobile("Test-1 Mobile : 9563359659");
		emp.setSalary(110000);
		
		return emp;
	}	
	
//--------------------------------------------------------------------------------------------------------------TRACE METHOD-------------

	@RequestMapping(value = "/testTraceMethod", method = RequestMethod.TRACE)
	@ResponseBody public Employee testTraceMethod() throws RestClientException, IOException
	{
		LOGGER.info("IN TEST TRACE METHOD CONTROLLER");
		
		Employee emp = new Employee();
		
		emp.setEmpId(021);
		emp.setName("Test-1 Name : RAJIB GARAI");
		emp.setEmail("Test-1 Email : rajib@gmail.com");
		emp.setMobile("Test-1 Mobile : 9563359659");
		emp.setSalary(110000);
		
		return emp;
	}	
	
	
//--------------------------------------------------------------------------------------------INDEX PAGE--------------------

	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	@ResponseBody public ModelAndView indexPage()
	{		
		LOGGER.info("In Index Page Controller");
		return new ModelAndView("index");
	}	
	
//--------------------------------------------------------------------------------------------INDEX PAGE--------------------

	@RequestMapping(value = "/empMicroservice", method = RequestMethod.GET)
	@ResponseBody public ModelAndView empMicroservicePage()
	{		
		LOGGER.info("In empMicroservice Page Controller");
		return new ModelAndView("empMicroservice");
	}
	
		
	
	
}
