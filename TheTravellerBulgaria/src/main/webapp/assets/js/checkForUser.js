
function checkForUser() {
	$.get(
		"GetUserInfo",
		function(data){
			if(data==""){
				document.getElementById("userButton").innerHTML="<a class=\"btnc\" href=\"SignIn.html\">SIGN IN / SIGN UP</a>";
			}else{
				var json = data;
				sessionStorage.setItem('user', json);
				sessionStorage.setItem('userEmail', json.email);
				 document.getElementById("userButton").innerHTML="<a class=\"btnc\" href=\"logout\" onclick=\"logOut()\">Logout</a>";
		            document.getElementById("secondButton").innerHTML="<a class=\"btnc\" href=\profile.html>PROFILE</a>";
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
			var src = ("GetProfilePicture?email="+ json.email).replace(" ", "%20");
			document.getElementById("profilePicture").innerHTML = "<div class=\"round img bg-img-c square\" style=\"width: 150px; background-image: url("+src+")\"></div>";
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



