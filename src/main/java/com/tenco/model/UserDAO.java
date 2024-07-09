package com.tenco.model;

import java.util.List;

public interface UserDAO {

	int addUser(UserDTO userDTO);
	UserDTO getUserById(int id);
	UserDTO getUserByUsername(String username);
	List<UserDTO> getAllUsers();
	// 인증검사 필요 (세션)
	int updateUser(UserDTO user, int principalId);// ... where id = ?
	int deleteUser(int id);
	
}
