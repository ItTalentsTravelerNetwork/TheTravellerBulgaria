
function checkForUser() {
	$.get(
		"GetUserInfo",
		function(data){
			if(data==null){
				document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"SignIn.html\">SIGN IN / SIGN UP</a>";
			}else{
				var json = JSON.parse(data);
				sessionStorage.setItem('user', json);
				 document.getElementById("userButton").innerHTML="<a class=\"btn\" href=\"logout\">Logout</a>" +
		            "<a class=\"btn\" href=\profile.html\">PROFILE</a>";
			}
		}
	)
}

function checkIfLoggedIn() {
	if(sessionStorage.getItem('user') == null){
        window.location.href = "SignIn.html";
    }
}

