<%@page import="com.tenco.model.TodoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo 상세 보기 화면</title>
<link rel="stylesheet" type="text/css" href="../css/todoDetailStyles.css">
</head>
<body>
	<h2>상세 보기</h2>
	<%
		TodoDTO todoDTO = (TodoDTO) request.getAttribute("todo");
		
		if (todoDTO != null) {
	%>
			
		<p>제목 : <%=todoDTO.getTitle() %></p><br>
		<p>설명 : <%=todoDTO.getDescription() %></p><br>
		<p>마감일 : <%=todoDTO.getDueDate() %></p><br>
		<p>완료 여부 : <%=todoDTO.completedToString() == "true" ? "완료" : "미완료" %></p><br>
		<hr><br>	
		
		<h2>수정하기</h2>
		<form action="update" method="post">
			<input type="hidden" name="id" value="<%= todoDTO.getId() %>">
			<label for="title">제목 : </label>
			<input type="text" id="title" name="title" value="<%= todoDTO.getTitle() %>">
			<br>
			<label for="description">설명 : </label>
			<input type="textarea" id="description" name="description" value="<%= todoDTO.getDescription() %>">
			<br>
			<label for="dueDate">마감일 : </label>
			<input type="date" id="dueDate" name="dueDate" value="<%= todoDTO.getDueDate() %>">
			<br>
			<div>
			<label for="completed">완료 여부 : </label>
			<input type="checkbox" id="completed" name="completed" <%= todoDTO.completedToString() == "true" ? "checked" : "" %> >
			</div>
			<br>
			<button type="submit">수정</button>
		</form>
			
		<% } else {
			out.print("<p> 정보를 불러오는데 실패 </p>");
			}
		%>
		
</body>
</html>