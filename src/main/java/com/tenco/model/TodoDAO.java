package com.tenco.model;

import java.util.List;

public interface TodoDAO {

	// 저장 기능
	void addTodo(TodoDTO dto, int principalId);
	
	TodoDTO getTodoById(int id);
	
	// 사용자 아이디 기준으로 todoList 출력
	List<TodoDTO> getTodosByUserId(int userId);
	
	// 모든 todoList 출력
	List<TodoDTO> getAllTodos();
	
	void updateTodo(TodoDTO dto, int principalId);
	
	void deleteTodo(int id, int principalId);
	
}
