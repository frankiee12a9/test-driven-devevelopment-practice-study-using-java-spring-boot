package com.ivanfranchin.movieapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ivanfranchin.movieapi.model.payload.ApiResponse;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	private ApiResponse apiResponse;

	private String message;

	public UnauthorizedException(ApiResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public UnauthorizedException(String message) {
		super(message);
		this.message = message;
	}

	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(ApiResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}   
}
