function checkForUser() {
    if(sessionStorage.getItem('user') == null){
        document.getElementById("userButton").innerHTML="<li><a class=\"btn\" href=\"SignIn.html\">SIGN IN / SIGN UP</a></li>";
    }else{
        document.getElementById("userButton").innerHTML="<li><a class=\"btn\" href=\"logout\">Logout</a></li>" +
            "<li><a class=\"btn\" href=\profile.html\">PROFILE</a></li>";
    }
}

function checkIfLoggedIn() {
	if(sessionStorage.getItem('user') == null){
        window.location.href = "SignIn.html";
    }
}
