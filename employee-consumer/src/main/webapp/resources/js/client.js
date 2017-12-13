/**
 * Created by stephan on 20.03.16.
 */

$(function () {
    // VARIABLES =============================================================
    var TOKEN_KEY = "jwtToken"
    var $notLoggedIn = $("#notLoggedIn");
    var $loggedIn = $("#loggedIn").hide();
    var $loggedInBody = $("#loggedInBody");
    var $response = $("#response");
    var $login = $("#login");
    var $userInfo = $("#userInfo").hide();

    // FUNCTIONS =============================================================
    function getJwtToken() {
    	console.log("localStorage.getItem(TOKEN_KEY) : "+localStorage.getItem(TOKEN_KEY));    	
        return localStorage.getItem(TOKEN_KEY);
    }

    function setJwtToken(token) {
        localStorage.setItem(TOKEN_KEY, token);
    }

    function removeJwtToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

    function doLogin(loginData) 
    {
    	console.log("In doLogin => (/auth)");
    	
    	console.log("Login Data : "+loginData.username+" AND "+loginData.password);
    	
        $.ajax({
            url: "/auth",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, textStatus, jqXHR) 
            {
                setJwtToken(data.token);
                $login.hide();
                $notLoggedIn.hide();
                showTokenInformation();
                showUserInformation();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401) {
                    $('#loginErrorModal')
                            .modal("show")
                            .find(".modal-body")
                            .empty()
                            .html("<p>Spring exception :<br>" + jqXHR.responseJSON.exception + "</p>");
                } else {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    }

    function doLogout() {
        removeJwtToken();
        $login.show();
        $userInfo
                .hide()
                .find("#userInfoBody").empty();
        $loggedIn.hide();
        $loggedInBody.empty();
        $notLoggedIn.show();
    }

    function createAuthorizationTokenHeader() {
        var token = getJwtToken();
        if (token) {
            return {"Authorization": token};
        } else {
            return {};
        }
    }

    function showUserInformation() {
    	
    	console.log("In showUserInformation => (/user)");
    	
        $.ajax({
            url: "/user",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) 
            {
                var $userInfoBody = $userInfo.find("#userInfoBody");

                $userInfoBody.append($("<div>").text("Username: " + data.username));
                $userInfoBody.append($("<div>").text("Email: " + data.email));

                var $authorityList = $("<ul>");
                data.authorities.forEach(function (authorityItem) {
                    $authorityList.append($("<li>").text(authorityItem.authority));
                });
                var $authorities = $("<div>").text("Authorities:");
                $authorities.append($authorityList);

                $userInfoBody.append($authorities);
                $userInfo.show();
            }
        });
    }

    function showTokenInformation() 
    {
        var jwtToken = getJwtToken();
        var decodedToken = jwt_decode(jwtToken);
        
        console.log("Encoded JWT Token : "+jwtToken);
        console.log("Decoded JWT Token : "+decodedToken);

        $loggedInBody.append($("<h4>").text("Token"));
        $loggedInBody.append($("<div>").text(jwtToken).css("word-break", "break-all"));
        $loggedInBody.append($("<h4>").text("Token claims"));

        var $table = $("<table>")
                .addClass("table table-striped");
        appendKeyValue($table, "sub", decodedToken.sub);
        appendKeyValue($table, "audience", decodedToken.audience);
        appendKeyValue($table, "created", new Date(decodedToken.created).toString());
        appendKeyValue($table, "exp", decodedToken.exp);

        $loggedInBody.append($table);

        $loggedIn.show();
    }

    function appendKeyValue($table, key, value) {
        var $row = $("<tr>")
                .append($("<td>").text(key))
                .append($("<td>").text(value));
        $table.append($row);
    }

    function showResponse(statusCode, message) {
    	
    	console.log("StatusCode : "+statusCode);
    	console.log("Message : "+message);
    	
        $response
                .empty()
                .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }

    // REGISTER EVENT LISTENERS =============================================================
    
    $("#loginForm").submit(function (event) {
    	
    	alert("In Login Form ");
    	
    	console.log("In Login Form");
    	
        event.preventDefault();

        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };

        doLogin(formData);
    });

    $("#logoutButton").click(doLogout);

    $("#exampleServiceBtn").click(function () {
    	
    	console.log("In Eexample Service => (/persons)");
    	
        $.ajax({
            url: "/persons",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR)
            {
            	console.log(" Data : "+JSON.stringify(data));
            	
                showResponse(jqXHR.status, JSON.stringify(data));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $("#adminServiceBtn").click(function () {
    	
    	console.log("In Admin Service => (/protected)");
    	
        $.ajax({
            url: "/protected",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponse(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $loggedIn.click(function () {
        $loggedIn
                .toggleClass("text-hidden")
                .toggleClass("text-shown");
    });

    // INITIAL CALLS =============================================================
    if (getJwtToken()) {
        $login.hide();
        $notLoggedIn.hide();
        showTokenInformation();
        showUserInformation();
    }
    
    $("#signUpForm").click(function () 
    {    	
    	var $form = $(this);
    	
    	var formData = {
                username: $form.find('input[name="username1"]').val(),
                password: $form.find('input[name="password1"]').val(),
                firstname: $form.find('input[name="firstname"]').val(),
                lastname: $form.find('input[name="lastname"]').val(),
                email: $form.find('input[name="email"]').val()
            };
    	
    	console.log(formData);
    	
    	alert("In Admin Sign Up Form");
    	
        $.ajax({
            url: "/auth/signUp",
            type: "POST",
            data: formData,
            contentType: "application/json; charset=utf-8",
            success: function (data, textStatus, jqXHR) 
            {
                showResponse(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) 
            {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });  
    
    
	$("#idGetPersons").click(function ()
	{   	
    	console.log("In Admin Service => (/employee-query/employee/getEmployees)");
    	
        $.ajax({
            url: "/employee-query/employee/getEmployees",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) 
            {
            	console.log(data);
                showResult(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) 
            {
            	showResult(jqXHR.status, errorThrown);
            }
        });
    });
    
	function showResult(statusCode, message) {	    	
		
		var data = "URL : http://localhost:8091/employee-query/employee/getEmployees\n\n";
		
    	for(var i=0; i<message.length; i++)
		{
    		data+="Name : "+message[i].name+" Email : "+message[i].email+"\n";
		}
    	
    	$("#result")
                .empty()
                .text("status code: " + statusCode + "\n-------------------------\n" + data);
	    }
	
	//----------------------------------------------------------------------------------------------------------------------
			$("#signIn").click(function ()
			{ 					
				var username = $('#username').val();
				var password = $('#password').val();
					
				alert("username : "+username);
				
				  $.ajax({
						type: "POST",
						url: "/employee/auth",
						data: ({						
									username : username,
									password : password							
						}),
						success: function(data) 
						{				
							console.log(data);				
							setJwtToken(data);
							
							window.location=("empMicroservice");
						},
				  		error:	function(e)
				  		{	  			
				  			console.log(e['responseText']);		  			
				  			alert(e['responseText']);	  			
				  		}
					});   
			});
		
			$("#signUp").click(function ()
			{	
				var firstname = $('#firstname').val();
				var lastname = $('#lastname').val();
				var email = $('#email').val();
				var password1 = $('#password1').val();
				
				 $.ajax({
							type: "POST",
							url: "/employee/signUp",
							data:  ({
										firstname : firstname,
										lastname : lastname,
										email : email,
										password : password1
									}),
							success: function(data) 
							{	
								alert("Successfully Registration => "+data)
							},
					  		error:	function(e)
					  		{
					  			console.log(e['responseText']);
					  		}
						}); 
				
			});
		
			$("#getEmployees").click(function ()
			{
				$.ajax({
					type: "GET",
					url: "/employee/getEmployees",
					headers: createAuthorizationTokenHeader(),
					success: function(data) 
					{	
						alert("Successfully Data => "+data);
						
						var output = "<ul>";
						
						for(var i=0; i<data.length; i++)
						{
							output += "<li>"+data[i].empId+" | "+data[i].name+" | "+data[i].email+" | "+data[i].role+" | "+data[i].mobile+"</li>";						
						}						
						output += "</ul>";
												
						$("#output").append(output);
						
						console.log(data);
					},
			  		error:	function(e)
			  		{
			  			console.log(e['responseText']);
			  		}
				}); 
			});
			
	//----------------------------------------------------------------------------------------------------------------------
	    
});

