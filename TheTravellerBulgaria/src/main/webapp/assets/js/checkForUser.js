
function checkForUser() {
	$.get(
		"GetUserInfo",
		function(data){
			if(data==""){
				document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"SignIn.html\">SIGN IN / SIGN UP</a>";
			}else{
				var json = data;
				sessionStorage.setItem('user', json);
				 document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"logout\">Logout</a>";
		            document.getElementById("secondButton").innerHTML="<a class=\"btn\" href=\profile.html>PROFILE</a>";
			}
		}
	)
}

function checkIfLoggedIn() {
	$.get(
			"GetUserInfo",
			function(data){
				if(data==""){
					window.location.href="SignIn.html";
				}
			}
	)
}

function getUserInfo(){
	$.get(
		"GetUserInfo",
		function(data){
			var json = data;
			document.getElementById("fullName").innerHTML = json.firstName + " " + json.lastName;
			document.getElementById("description").innerHTML = json.description;
			document.getElementById("profilePicture").innerHTML = "<img src=\"GetProfilePicture?email="+ json.email + "\" height=\"150\" width=\"150\" alt=\"\"/>";
		}
	)
}



