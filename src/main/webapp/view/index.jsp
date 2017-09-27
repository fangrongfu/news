<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>小度二代</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/news/result.do" method="post">
		<input type ="text" name ="title">
		<input type ="submit" value="小度搜索">
	</form>
	 <c:forEach items="${list}" var="news"> 
	 <div>${news.title}</div> 
	 <div>${news.time}</div>
    </c:forEach> 
</body>
</html>