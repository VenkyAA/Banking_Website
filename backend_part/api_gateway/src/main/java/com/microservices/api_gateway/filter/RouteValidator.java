package com.microservices.api_gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
	
	public static final List<String> openApiEndpoints = List.of(
		
			"/users/register",
			"/users/login",
			"/eureka",
			"/accounts/create"
			
	);
	
	public Predicate<ServerHttpRequest> isSecured = 
			request -> openApiEndpoints
					.stream()
					.noneMatch(uri -> request.getURI().getPath().contains(uri));

	
			

}
