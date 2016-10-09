
function checkForUser() {
	$.get(
		"GetUserInfo",
		function(data){
			if(data==""){
				document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"SignIn.html\">SIGN IN / SIGN UP</a>";
			}else{
				var json = data;
				sessionStorage.setItem('user', json);
				sessionStorage.setItem('userEmail', json.email);
				 document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"logout\" onclick=\"logOut()\">Logout</a>";
		            document.getElementById("secondButton").innerHTML="<a class=\"btn\" href=\profile.html>PROFILE</a>";
		            document.getElementById("newsFeed").innerHTML="<a href=\NewsFeed.html>News Feed</a>"
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
			document.getElementById("profilePicture").innerHTML = "<img src=\"GetProfilePicture?email="+ json.email + "\" height=\"150\" width=\"150\" alt=\"\" style=\"border-radius: 50%;\"/>";
		}
	)
}

function logOut(){
	$.get(
		"logout",
		function(data){
			sessionStorage.clear();
			window.location.href="SignIn.html";
		}
	)
	
}



