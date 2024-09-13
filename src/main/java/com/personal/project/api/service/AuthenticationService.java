package com.personal.project.api.service;

import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.repository.UserRepository;
import com.personal.project.api.service.exceptions.AuthorizationException;
import com.personal.project.api.service.exceptions.BusinessException;
import com.personal.project.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.personal.project.api.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Lazy
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;


    public User findAuthenticatedUser() {

        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
        if (userAuth == null)
            throw new AuthorizationException("Access denied!");

        User authenticatedUser = (User) userAuth.getPrincipal();

        return userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "User not found! Id: " + authenticatedUser.getId() + ", Type: " + User.class.getName()));

    }

    public void authenticateUserByTokenData(String login) {

        UserDetails user = userRepository.findByLogin(login);
        if(user == null)
            throw new BusinessException("User not found for login: " + login);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public User authenticateUserByLoginData(RequestUserLoginDTO authDTO) {
        var userPass = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var auth = authManager.authenticate(userPass);
        return (User)auth.getPrincipal();
    }

}