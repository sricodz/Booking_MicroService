package com.user.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

	@Id
	private String id;
	private String name;
	private Address address;
	private Long phoneNumber;
	
	
}
