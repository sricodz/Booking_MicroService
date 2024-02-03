package com.booking.service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

	private String id;
	private String restaurantName;
	private String location;
	private String type;
	private List<BookTable> tables;
}
