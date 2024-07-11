package com.tenco.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.model.TodoDAO;
import com.tenco.model.TodoDAOImpl;
import com.tenco.model.TodoDTO;
import com.tenco.model.UserDAO;
import com.tenco.model.UserDAOImpl;
import com.tenco.model.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test/*")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private TodoDAO todoDAO;
	
    public TestController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	userDAO = new UserDAOImpl();
    	todoDAO = new TodoDAOImpl();
    }

    // http://localhost:8080/mvc/test/t1
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();

		// TodoDTO todoDTO = todoDAO.getTodoById(100);
		// System.out.println(todoDTO.toString());
		// List<TodoDTO> list = todoDAO.getTodosByUserId(8);
		// System.out.println(list.toString());
		// System.out.println(todoDAO.getAllTodos());
		// todoDAO.updateTodo(new TodoDTO(), 8);
		todoDAO.deleteTodo(8, 8);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
