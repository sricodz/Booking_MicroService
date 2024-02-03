package com.restaurant.service.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Restaurant {

	@Id
	private String id;
	private String restaurantName;
	private String location;
	private String type;
	private List<BookTable> tables;
}
