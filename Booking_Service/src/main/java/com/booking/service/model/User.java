package com.booking.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private String id;
	private String name;
	private Address address;
	private Long phoneNumber;
}
