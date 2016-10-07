
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
	if(sessionStorage.getItem('user') == null){
        window.location.href = "SignIn.html";
    }
}



