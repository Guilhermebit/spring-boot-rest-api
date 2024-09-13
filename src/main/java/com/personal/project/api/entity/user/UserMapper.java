package com.personal.project.api.entity.user;

import com.personal.project.api.controller.dto.user.ResponseUserRegisterDTO;
import com.personal.project.api.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ResponseUserRegisterDTO mapToResponseUserRegisterDTO(User user) {
        if (user == null)
            return null;

        return new ResponseUserRegisterDTO(
                user.getId(),
                user.getUsername()
        );
    }
}