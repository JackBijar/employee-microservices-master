<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee Consumer</title>
</head>
<body>
	<div align="center">
	
		<h2>:: User Login :: </h2>	
		<h4>--------------------------------------------------------------------------</h4>		
		<div>
		  <h4>User Name : <input type="text" class="form-control" id="username" placeholder="User Name" required name="username"></h4>
          <h4>Password : <input type="password" class="form-control" id="password" placeholder="Password" required name="password"></h4>
                  
          <button id="signIn" type="submit">Login</button>
                        
            <h5>: Try one of the following logins : </h5>                 
            <h6>admin & admin || user & password</h6> 
        </div>
        
        <h4>--------------------------------------------------------------------------------------------</h4>        
        	
        <div><h2> :	REGISTRATION	: </h2>
        
        <h4>--------------------------------------------------------------------------------------------</h4>
                 
         	<h4>First Name : <input type="text" class="form-control" id="firstname" placeholder="First Name" required name="firstname">  </h4>      
          
            <h4>Last Name : <input type="text" class="form-control" id="lastname" placeholder="Last Name" required name="lastname"></h4>
          
            <h4>Email : <input type="text" class="form-control" id="email" placeholder="Email" required name="email"></h4>
                         
            <h4>Password : <input type="password" class="form-control" id="password1" placeholder="Password" required name="password1">  </h4>      
                             
          	<button type="submit" id="signUp" class="btn btn-default">Sign Up</button>	   	
         </div>
         
         <h4>----------------------------------------------------------------------------------------------------------------</h4><br>
		
	</div>
	
	
	<script src="https://code.jquery.com/jquery-2.2.2.js" integrity="sha256-4/zUCqiq0kqxhZIyp4G0Gk+AOtCJsY1TA00k5ClsZYE="
        crossorigin="anonymous"></script>
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	        crossorigin="anonymous"></script>
	<script src="/resources/js/libs/jwt-decode.min.js"></script>
	<script src="/resources/js/client.js"></script>
	
</body>
</html>