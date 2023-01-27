package com.ivanfranchin.movieapi.mapper;

import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.rest.dto.UserDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-27T19:01:05+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.3.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String name = null;
        String email = null;
        String role = null;

        id = user.getId();
        username = user.getUsername();
        name = user.getName();
        email = user.getEmail();
        role = user.getRole();

        UserDto userDto = new UserDto( id, username, name, email, role );

        return userDto;
    }
}
