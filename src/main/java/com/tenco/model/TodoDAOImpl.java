package com.tenco.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TodoDAOImpl implements TodoDAO {
	
	private DataSource dataSource;
	
	public TodoDAOImpl() {

		try {
			InitialContext ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void addTodo(TodoDTO dto, int principalId) {
		String sql = " insert into todos(user_id, title, description, due_date, completed) values(?, ?, ?, ?, ?) ";
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, principalId);
				pstmt.setString(2, dto.getTitle());
				pstmt.setString(3, dto.getDescription());
				pstmt.setString(4, dto.getDueDate());
				pstmt.setBoolean(5, dto.getCompleted());
				pstmt.executeUpdate();

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TodoDTO getTodoById(int id) {
		String sql = " select * from todos where id = ? ";
		TodoDTO todoDTO = null;
		
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					todoDTO = new TodoDTO();
					todoDTO.setId(rs.getInt("id"));
					todoDTO.setUserId(rs.getInt("user_id"));
					todoDTO.setTitle(rs.getString("title"));
					todoDTO.setDescription(rs.getString("description"));
					todoDTO.setDueDate(rs.getString("due_date"));
					todoDTO.setCompleted(rs.getBoolean("completed"));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return todoDTO;
	}

	@Override
	public List<TodoDTO> getTodosByUserId(int userId) {
		String sql = " select * from todos where user_id = ? ";
		List<TodoDTO> list = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					TodoDTO todoDTO = new TodoDTO();
					todoDTO.setId(rs.getInt("id"));
					todoDTO.setTitle(rs.getString("title"));
					todoDTO.setDescription(rs.getString("description"));
					todoDTO.setDueDate(rs.getString("due_date"));
					todoDTO.setCompleted(rs.getBoolean("completed"));
					todoDTO.setUserId(rs.getInt("user_id"));
					list.add(todoDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("TodoList : " + list.toString());
		return list;
	}

	@Override
	public List<TodoDTO> getAllTodos() {
		String sql = " select * from todos ";
		List<TodoDTO> list = new ArrayList<>();
		
		try (Connection conn = dataSource.getConnection()) {
			try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					TodoDTO todoDTO = new TodoDTO();
					todoDTO.setId(rs.getInt("id"));
					todoDTO.setTitle(rs.getString("title"));
					todoDTO.setDescription(rs.getString("description"));
					todoDTO.setDueDate(rs.getString("due_date"));
					todoDTO.setCompleted(rs.getBoolean("completed"));
					todoDTO.setUserId(rs.getInt("user_id"));
					list.add(todoDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("TodoList All : " + list.toString());
		return list;
	}

	@Override
	public void updateTodo(TodoDTO dto, int principalId) {
		int rowCount = 0;
		String sql = " update todos set title = ? where user_id = ? ";
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, dto.getTitle());
				pstmt.setInt(2, principalId);
				rowCount = pstmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteTodo(int id, int principalId) {
		int rowCount = 0;
		String sql = " delete from todos where id = ? ";
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, id);
				rowCount = pstmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
