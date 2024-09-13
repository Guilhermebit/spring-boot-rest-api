package com.personal.project.api.service;

import com.personal.project.api.config.JWTUtil;
import com.personal.project.api.config.SecurityUtil;
import com.personal.project.api.controller.dto.user.ResponseUserLoginDTO;
import com.personal.project.api.controller.dto.user.ResponseUserRegisterDTO;
import com.personal.project.api.entity.user.UserMapper;
import com.personal.project.api.entity.user.User;
import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.controller.dto.user.RequestUserRegisterDTO;
import com.personal.project.api.repository.UserRepository;
import com.personal.project.api.service.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public ResponseUserRegisterDTO registerUser(@Valid RequestUserRegisterDTO requestUserRegisterDTO) {
        if(userRepository.findByLogin(requestUserRegisterDTO.login()) != null)
           throw new BusinessException("User already registered!");

        User newUser = new User(requestUserRegisterDTO.login(),
                securityUtil.encodePassword(requestUserRegisterDTO.password()), requestUserRegisterDTO.role());
        return userMapper.mapToResponseUserRegisterDTO(userRepository.save(newUser));
    }

    public ResponseUserLoginDTO loginUser(@Valid RequestUserLoginDTO authDTO) {
        User loginUser = authenticationService.authenticateUserByLoginData(authDTO);
        return new ResponseUserLoginDTO(jwtUtil.generateToken(loginUser));
    }

}