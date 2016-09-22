<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form role="form"
		action="${pageContext.request.contextPath}/playThisSong" method="POST">
		<select name="songId">
			<option value="501357">501357</option>
			<option value="501358">501358</option>
			<option value="501360">501360</option>
			<option value="501354">501354</option>
			<option value="501352">501352</option>

		</select>
		<button type="submit">Play</button>
	</form>
</body>
</html>