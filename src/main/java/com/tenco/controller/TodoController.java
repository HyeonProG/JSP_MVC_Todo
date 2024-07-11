package com.tenco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    // http://localhost:8080/mvc/todo/todoForm (권장 x)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		
		// 로그인한 사용자만 접근을 허용하도록 설계
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO)session.getAttribute("principal");
		
		// 인증 검사
		if (principal == null) {
			// 로그인을 하지 않은 상태
			response.sendRedirect("/mvc/user/signIn?message=invalid");
			return;
		}
		
		switch (action) {
		case "/todoForm":
			todoFormPage(request, response);
			break;
		case "/list":
			todoListPage(request, response, principal.getId());
			break;
		case "/detail":
			todoDetailPage(request, response, principal.getId());
			break;
		case "/delete":
			deleteTodo(request, response, principal.getId());
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");
		
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?message=invalid");
			return;
		}
		
		String action = request.getPathInfo();

		switch (action) {
		case "/add":
			addTodo(request, response, principal.getId());
			break;
		case "/update":
			updateTodo(request, response, principal.getId());
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
				
	}


	/**
	 * todo 작성 페이지 이동
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void todoFormPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		request.getRequestDispatcher("/WEB-INF/views/todoForm.jsp").forward(request, response);
		
	}
	
	/**
	 * 사용자별 todo 리스트 페이지
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	// http://localhost:8080/mvc/todo/list
	private void todoListPage(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException, ServletException {
				
		// request.getParameter() --> URL 요청에 있어 데이터 추출
		// request.getPathInfo() --> URL 요청에 있어 데이터 추출
		// request.getAttribute() --> 뷰를 내릴 속성에 값을 담아서 뷰로 내릴 때 사용

		// 데이터를 담아서 던질 예정(DB 조회)
		List<TodoDTO> list = todoDAO.getTodosByUserId(principalId);
		request.setAttribute("list", list);
		
		// todoList.jsp 페이지로 내부에서 이동 처리
		request.getRequestDispatcher("/WEB-INF/views/todoList.jsp").forward(request, response);
	}
	
	/**
	 * 상세 보기 화면
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	
	// http://localhost:8080/mvc/todo/detail?id=2
	private void todoDetailPage(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		
		try {
			// todo - pk - 여러개
			int todoId = Integer.parseInt(request.getParameter("id"));
			TodoDTO todoDTO = todoDAO.getTodoById(todoId);
			if (todoDTO.getUserId() == principalId) {
				// 상세보기 화면으로 이동 처리
				request.setAttribute("todo", todoDTO);
				request.getRequestDispatcher("/WEB-INF/views/todoDetail.jsp").forward(request, response);
			} else {
				// 권한 없음 처리 or 잘못된 접근 처리
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script> alert('권한이 없습니다'); history.back(); </script>");
			}
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/todo/list?error=invalid");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 세션별 사용자 todo 등록
	 * @param request
	 * @param response
	 * @param principalId : 세션에 담겨있는 userId 값
	 * @throws IOException 
	 */
	private void addTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDate = request.getParameter("due_date");
		// checkBox는 여러개 선택 가능한 태그 : String[] 로 선언 했다
		// 이번 checkBox는 하나만 사용 중
		// checkBox가 선택되지 않았으면 null을 반환하고 선택되면 "on"을 반환
		boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));
		System.out.println("completed : " + completed);
		
		TodoDTO todoDTO = TodoDTO.builder()
							.userId(principalId)
							.title(title)
							.description(description)
							.dueDate(dueDate)
							.completed(String.valueOf(completed))
							.build();
		
		todoDAO.addTodo(todoDTO, principalId);
		
		response.sendRedirect(request.getContextPath() + "/todo/list");
	}
	
	/**
	 * todo 삭제 기능
	 * @param request
	 * @param response
	 * @param principalId
	 * @throws IOException
	 */
	private void deleteTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		
		try {
			int todoId = Integer.parseInt(request.getParameter("id"));			
			 todoDAO.deleteTodo(todoId, principalId);
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/todo/list?error=invalid");
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/todo/list");		
	}
	
	/**
	 * todo 수정 기능
	 * @param request
	 * @param response
	 * @param principalId - 세션 ID 값
	 * @throws IOException
	 */
	private void updateTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		
		try {
			int todoId = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String dueDate = request.getParameter("dueDate");
			boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));
			System.out.println("completed : " + completed);
			
			TodoDTO todoDTO = TodoDTO.builder()
								.id(todoId)
								.userId(principalId)
								.title(title)
								.description(description)
								.dueDate(dueDate)
								.completed(String.valueOf(completed))
								.build();
			
			 todoDAO.updateTodo(todoDTO, principalId);
			
		} catch (Exception e) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('잘못된 요청입니다'); history.back(); </script>");
		}
		// list 화면 재 요청 처리
		response.sendRedirect(request.getContextPath() + "/todo/list");
		
	}
	
}
