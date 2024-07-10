package com.tenco.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TodoDTO {

	private int id;
	private int userId;
	private String title;
	private String description;
	private String dueDate;
	private Boolean completed;
	
}
