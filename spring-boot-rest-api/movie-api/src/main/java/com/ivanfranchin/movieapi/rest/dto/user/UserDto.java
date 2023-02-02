package com.ivanfranchin.movieapi.rest.dto.user;

public record UserDto(Long id, String username, String name, String email, String role) {
}