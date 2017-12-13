<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<div align="center">
		
		<h3>::	Employee Microservices :: </h3> <h4>[User | Authority | Employee]</h4>
		<h4>--------------------------------------------------------------------------------------------</h4>	
		
		<h5>||	<a href="#" id="getEmployees">Get Employees</a>	||	
						
		<a href="getEmployee">Get Employee By ID</a>	||
			
		<a href="getEmployeeByEmail">Get Employee By Email</a> ||	<br><br>
		
		<a href="getEmployeeFall">Get Employee Fall</a>	||		
		<a href="getEmployeeFall">Get Employee Fall By ID</a>	||	<br><br>
		
		<a href="updateEmployee">Update Employee</a> 	||
		<a href="deleteEmployee">Delete Employee</a>	||			<br><br>
		
		<a href="testHeadMethod">Test Head Method</a>	||		
		<a href="testOptionMethod">Test Option Method</a>	||
		<a href="testPatchMethod">Test Patch Method</a>		||
		
		<a href="testTraceMethod">Test Trace Method</a>	</h5>
					
		<h4>---------------------------------------------------------------------------------------------------------</h4>
				<h3>: OUT UPT :</h3> 
				<h3>...................................................................................</h3>
					<div id="output"></div>
				<h3>...................................................................................</h3>
				
		<h4>---------------------------------------------------------------------------------------------------------</h4>		
	
	</div>
	
	<script src="https://code.jquery.com/jquery-2.2.2.js" integrity="sha256-4/zUCqiq0kqxhZIyp4G0Gk+AOtCJsY1TA00k5ClsZYE="
        crossorigin="anonymous"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	        crossorigin="anonymous"></script>
	<script src="/resources/js/libs/jwt-decode.min.js"></script>
	<script src="/resources/js/client.js"></script>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	
</body>
</html>