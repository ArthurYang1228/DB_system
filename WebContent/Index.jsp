<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include  file="InHead.html" %>
<style>
	html, body {
		background-image: url(henry.jpeg);
	}
	#title {
		background-color: rgba(255, 255, 255, 0.9);
		width: 80%;
		height: 400px;
		margin: 100px auto;
		text-align: center;
		padding: 10%;
	}
	#h1 {
		font-size: 54px;
	}
</style>
</head>
<body>
	<%@ include  file="Header.html" %>
	
	<div id="title">
		<h1 id="h1"> 飲料店進銷存資訊系統 </h1>
		<br/>
		<h2>組員</h2>
		<h3>呂長恩、黃凱文、張濟任、陽承霖、陳韋璿 </h3>
	</div>
</body>
</html>