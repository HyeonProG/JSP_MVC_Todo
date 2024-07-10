package com.tenco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.tenco.model.TodoDAO;
import com.tenco.model.TodoDAOImpl;
import com.tenco.model.TodoDTO;
import com.tenco.model.UserDTO;

// .../mvc/todo/xxx
@WebServlet("/todo/*")
public class TodoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TodoDAO todoDAO;
       
    public TodoController() {
    	todoDAO = new TodoDAOImpl(); // 업캐스팅
    }

    // http://localhost:8080/mvc/todo/todoForm
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		switch (action) {
		case "/todoForm":
			todoFormPage(request, response);
			break;
		case "/list":
			todoListPage(request, response);
		default:
			break;
		}
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDate = request.getParameter("due_date");
		boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
		
		TodoDTO todoDTO = TodoDTO.builder()
				.title(title)
				.description(description)
				.dueDate(dueDate)
				.completed(completed)
				.build();
		
		// TODO - 수정 예정
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");
		// princial == null --> 로그인 페이지 이동 처리
		todoDAO.addTodo(todoDTO, principal.getId());	
		
	}

	private void todoFormPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// 로그인한 사용자만 접근을 허용하도록 설계
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO)session.getAttribute("principal");
		
		// 인증 검사
		if (principal == null) {
			// 로그인을 하지 않은 상태
			response.sendRedirect("/mvc/user/signIn?message=invalid");
			return;
		}
		request.getRequestDispatcher("/WEB-INF/views/todoForm.jsp").forward(request, response);
//		todoDAO.addTodo(todoDTO, 8);
		
	}
	
	// http://localhost:8080/mvc/todo/list
	private void todoListPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// 인증 검사
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO)session.getAttribute("principal");
		System.out.println("request.getContextPath() : " + request.getContextPath());
		
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?message=invalid");
			return;
		}
		// todoList.jsp 페이지로 내부에서 이동 처리
		request.setAttribute("todoList", todoDAO.getTodosByUserId(principal.getId()));
		System.out.println(todoDAO.getTodoById(principal.getId()));
		request.getRequestDispatcher("/WEB-INF/views/todoList.jsp").forward(request, response);
	}
}
