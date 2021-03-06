<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page errorPage="Error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport"    content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author"      content="David & Yasen">
	
	<title>Register in The Traveler Bulgaria</title>

	<link rel="shortcut icon" href="assets/images/gt_favicon.png">
	
	<link rel="stylesheet" media="screen" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
	<link rel="stylesheet" href="assets/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/css/font-awesome.min.css">

	<!-- Custom styles for our template -->
	<link rel="stylesheet" href="assets/css/bootstrap-theme.css" media="screen" >
	<link rel="stylesheet" href="assets/css/main.css">

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="assets/js/html5shiv.js"></script>
	<script src="assets/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top headroom" >
		<div class="container">
			<div class="navbar-header">
				<!-- Button for smallest screens -->
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
				<a class="navbar-brand" href="index.jsp"><img src="assets/images/logo4.png" alt="Progressus HTML5 template"></a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav pull-right">
					<li><a href="index.jsp">Home</a></li>
					<li><a href="AllDestinations.jsp">Destinations</a></li>
					<li class="active"><a class="btn" href="SignIn.jsp">SIGN IN / SIGN UP</a></li>
				</ul>
			</div><!--/.nav-collapse -->
		</div>
	</div> 
	<!-- /.navbar -->

	<header id="head" class="secondary"></header>

	<!-- container -->
	<div class="container">

		<ol class="breadcrumb">
			<li><a href="index.jsp">Home</a></li>
			<li class="active">Registration</li>
		</ol>

		<div class="row">
			
			<!-- Article main content -->
			<article class="col-xs-12 maincontent">
				<header class="page-header">
					<h1 class="page-title">Registration</h1>
				</header>
				
				<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
					<div class="panel panel-default">
						<div class="panel-body">
							<h3 class="thin text-center">Register a new account</h3>
							<p class="text-center text-muted">or <a href="SignIn.jsp">Sign in</a> to your account</p>
							<hr>

							<form id="regForm" action="javascript:add()" enctype="multipart/form-data">
							    <div class="top-margin">
							        <label>First Name <span class="text-danger">*</span></label>
							        <input id="userFirstName" type="text" name="userFirstName" class="form-control" maxlength="20" pattern="^[A-Za-z]+$" required>
							    </div>
							    
							    <div class="top-margin">
							        <label>Last Name <span class="text-danger">*</span></label> 
							        <input id="userLastName" type="text" name="userLastName" class="form-control" maxlength="20" pattern="^[A-Za-z]+$" required>
							    </div>
							    
							    <div class="top-margin">
							        <label>Email Address <span class="text-danger">*</span></label>
							        <input id="userEmailAddress" type="text" name="userEmailAddress" class="form-control" maxlength="40" pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+.[a-z.]+$" required>
							    </div>
							
							    <div class="row top-margin">
							        <div class="col-sm-6">
							            <label>Password <span class="text-danger">*</span></label>
							            <input id="userPassword" type="password" name="userPassword" class="form-control" maxlength="40" pattern="(.+){6,}" required>
							        </div>
							    </div>
							    
							    <div class="row top-margin">
							        <label>Description <span class="text-danger">*</span></label>
							        <input id="userDescription" type="textarea" name="userDescription" class="form-control" maxlength="500" required>
							    </div>
							    
							   
							
							    <div class="row">
							        <label>Picture <span class="text-danger">*</span></label>
							        <input id="profilePic" type="file" name="profilePic" class="form-control" required>
							    </div>
							     <div id="message" class="row top-margin">
							        
							    </div>
							    <hr>
							
							    <div class="row">
							        <div class="col-lg-4 text-right">
							            <button class="btn btn-action" type="submit">Register</button>
							        </div>
							    </div>
							</form>
							
						</div>
					</div>
				</div>
				
			</article>
			<!-- /Article -->

		</div>
	</div>	<!-- /container -->
	

	<footer id="footer" class="top-space">

        <div class="footer1">
            <div class="container">
                <div class="row">
                    
                    <div class="col-md-3 widget">
                        <h3 class="widget-title">Contact</h3>
                        <div class="widget-body">
                            <p><br>
                                <a href="mailto:#">thetravellerbulgaria@gmail.com</a><br>
                                <br>
                                Infinity Tower, Sofia City, Bulgaria
                            </p>    
                        </div>
                    </div>

                    <div class="col-md-3 widget">
                        <h3 class="widget-title">Follow us</h3>
                        <div class="widget-body">
                            <p class="follow-me-icons">
                                <a href=""><i class="fa fa-twitter fa-2"></i></a>
                                <a href=""><i class="fa fa-dribbble fa-2"></i></a>
                                <a href=""><i class="fa fa-github fa-2"></i></a>
                                <a href="https://www.facebook.com/TheTravellerBulgaria/"><i class="fa fa-facebook fa-2"></i></a>
                            </p>    
                        </div>
                    </div>

                    <div class="col-md-6 widget">
                        <h3 class="widget-title">Our motto</h3>
                        <div class="widget-body">
                            <p>"The whole object of travel is not to set foot on foreign land; it is to set foot on one's own country as on a foreign land." - G.K. Chesterton</p>
                            <p>So keep moving! But don't forget to take time to stop and smell the roses!</p>
                        </div>
                    </div>

                </div> <!-- /row of widgets -->
            </div>
        </div>

        <div class="footer2">
            <div class="container">
                <div class="row">
                    
                    <div class="col-md-6 widget">
                        <div class="widget-body">
                            <p class="simplenav">
                                <a href="#">Home</a> | 
                                <a href="SectionUnderConstructionPage.html">About</a> |
                                <a href="SectionUnderConstructionPage.html">Sidebar</a> |
                                <a href="SectionUnderConstructionPage.html">Contact</a> |
                                <b><a href="SignUp.jsp">Sign up</a></b>
                            </p>
                        </div>
                    </div>

                    <div class="col-md-6 widget">
                        <div class="widget-body">
                            <p class="text-right">
                                Copyright &copy; 2016, The Traveler Bulgaria. Designed by David & Yasen</a> 
                            </p>
                        </div>
                    </div>

                </div> <!-- /row of widgets -->
            </div>
        </div>

    </footer>
		




	<!-- JavaScript libs are placed at the end of the document so the pages load faster -->
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script src="assets/js/headroom.min.js"></script>
	<script src="assets/js/jQuery.headroom.min.js"></script>
	<script src="assets/js/template.js"></script>
	<script src="js/PictureValidate.js"></script>
	<script src="http://malsup.github.com/jquery.form.js"></script> 
</body>

<script>
function add(){
	var form = $("#regForm");
	form.ajaxSubmit({
		type : 'post',
		url : 'registration',
		async: true,
		success : function(results){
			if(results!=null && results != ""){
				showMessage(results);					
				
			}else{
				alert("Some error occured, try again.");
			}
		}
	})
}
	
function showMessage(results){		
        if(results == 'User Registration Successful!'){
            $('#message').html("<font color='green'>You have successfully registered. </font>")
            setTimeout(function(){
					window.location.href = "SignIn.jsp";
				}, 500)
        }else if(results == 'Registration failed!'){
            $('#message').html("<font color='red'>Incorrect data or picture format </font>")
        }
    }
</script>
</html>