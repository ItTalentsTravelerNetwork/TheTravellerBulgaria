<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Kiseli raboti s core</title>

<style>
	table, tr, td, thead, th {
		border: 2px solid green;
		border-collapse: collapse;
		padding: 6px;
		margin : 2px;
	}
</style>
</head>
<body>
	<table>
		<thead>
			<th> Name </th>
			<th> Quantity </th>
			<th> Photo </th>
		</thead>
		<c:forEach items="${products}" var="product">
			<tr>
				<td> ${product.name} </td>
				<td> ${product.quantity} </td>
				<td> <img src='<c:url value="${product.photo}"/>' width="20" height="20"/> </td>
			</tr>
		</c:forEach>
	</table>
	
	<form:form commandName="product">
		<label>Enter name:</label>
		<form:input path="name"/>
		<br/>
		<label>Enter quantity:</label>
		<form:input path="quantity"/>
		<br/>
		
		<input type="submit" value="Add new">
		
	</form:form>
</body>
</html>