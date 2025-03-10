package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateUserRequest {

	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private String confirmPassword;
}
