package com.spring.cloud.query.employee;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xmlpull.v1.XmlPullParserException;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.spring.cloud.query.employee.service.EmployeeService;
import com.spring.cloud.query.employee.service.EmployeeServiceImpl;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableSwagger2
public class EmployeeQueryApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(EmployeeQueryApplication.class, args);
	}
	
	@Bean
	public EmployeeService employeeService()
	{
		return new EmployeeServiceImpl();
	}
	
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}
	
	@Bean
    public Docket api() throws IOException, XmlPullParserException {
        return new Docket(DocumentationType.SWAGGER_2)  
          .select() 
          .apis(RequestHandlerSelectors.basePackage("com.spring.cloud.query.employee.controller"))
          .paths(PathSelectors.any())                          
          .build().apiInfo(apiInfo());                                           
    }
	
	/*@Bean
	public Docket postsApi() 
	{
		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
				.apiInfo(apiInfo()).select().paths(postPaths()).build();
		
		docket.globalResponseMessage(RequestMethod.GET, ImmutableList.of(new ResponseMessageBuilder()
				 .code(400)
				 .message("Bad Request")
				 .responseModel(new ModelRef("Error")).build(),new ResponseMessageBuilder()
				 .code(500)
				 .message("Internal Server Error")
				 .responseModel(new ModelRef("Error")).build()));
		
		return docket;
	}
	
	private Predicate<String> postPaths() {
		return or(regex("/employee/getEmployees.*"), regex("/employee/getEmployeeFall-1.*"),
				regex("/employee/getEmployeeFall-2.*"), regex("/employee/getEmployee/.*"),
				regex("/employee/getEmployeeByEmail/.*"));
	}*/

	private ApiInfo apiInfo() 
	{
		return new ApiInfoBuilder().title("Spring Cloud Employee Microservices")
				.description("Spring Cloud Employee Microservices with Swagger Configuration")
				.termsOfServiceUrl("http://www.casualcritivity.com")
				.contact("rajibgarai90@hotmail.com").license("Casual Critivity License")
				.licenseUrl("rajibgarai90@hotmail.com").version("1.0").build();
	}
}
