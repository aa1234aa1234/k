package com.example.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private String username;
	private String password;
	
	public User toEntity() {
		User user = User.builder()
				.name(getUsername())
				.password(getPassword())
				.build();
		return user;
	}
}
