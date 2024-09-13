package com.personal.project.api.unit;

import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.entity.user.User;
import com.personal.project.api.repository.UserRepository;
import com.personal.project.api.service.AuthenticationService;
import com.personal.project.api.service.exceptions.AuthorizationException;
import com.personal.project.api.service.exceptions.BusinessException;
import com.personal.project.api.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationServiceTest {

    @MockBean
    private AuthenticationManager authManager;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    UserRepository userRepository;

    /**
     * Method under test: {@link AuthenticationService#findAuthenticatedUser()}
     */

    @Test
    @DisplayName("Should return a user by id")
    void testFindById() {
        // Arrange
        User userObj = UTestData.createValidUser();
        createAuthentication(userObj);

        when(userRepository.findById(userObj.getId())).thenReturn(Optional.of(userObj));

        // Act
        User user = authenticationService.findAuthenticatedUser();

        // Assert
        assertNotNull(user);
        assertEquals(userObj.getId(), user.getId());
        verify(userRepository).findById(userObj.getId());

    }

    /**
     * Method under test: {@link AuthenticationService#findAuthenticatedUser()}
     */

    @Test
    @DisplayName("Should throw an exception when access is denied")
    void testAccessDenied() {

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AuthorizationException.class, () -> authenticationService.findAuthenticatedUser());
        verify(securityContext).getAuthentication();

    }

    /**
     * Method under test: {@link AuthenticationService#findAuthenticatedUser()}
     */

    @Test
    @DisplayName("Should throw an exception when the id is not founded")
    void testFindByIdNotFound() {
        // Arrange
        User userObj = UTestData.createValidUser();
        createAuthentication(userObj);

        when(userRepository.findById(userObj.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, ()-> authenticationService.findAuthenticatedUser());
        verify(userRepository).findById(userObj.getId());
    }

    /**
     * Method under test: {@link AuthenticationService#authenticateUserByTokenData(String)} (String)}
     */

    @Test
    @DisplayName("Should authenticate user when login is found")
    void testAuthenticateUserByTokenSuccess() {
        // Arrange
        User userObj = UTestData.createValidUser();
        when(userRepository.findByLogin(userObj.getLogin())).thenReturn(userObj);

        // Act
        authenticationService.authenticateUserByTokenData(userObj.getLogin());

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(userObj, authentication.getPrincipal());
        assertEquals(userObj.getAuthorities(), authentication.getAuthorities());
        verify(userRepository).findByLogin(userObj.getLogin());

    }

    /**
     * Method under test: {@link AuthenticationService#authenticateUserByTokenData(String)} (String)}
     */

    @Test
    @DisplayName("Should throw an exception when the login is not founded")
    void testAuthenticateUserByTokenInvalid() {
        // Arrange
        String invalidUser = "invalidUser";
        when(userRepository.findByLogin(invalidUser)).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> authenticationService.authenticateUserByTokenData(invalidUser));
        verify(userRepository).findByLogin(anyString());
    }

    /**
     * Method under test: {@link AuthenticationService#authenticateUserByLoginData(RequestUserLoginDTO)}
     */

    @Test
    @DisplayName("Should authenticated a user with valid data")
    void testAuthenticateUserWithValidData() {
        // Arrange
        User userObj = UTestData.createValidUser();
        RequestUserLoginDTO requestUserLoginDTO = new RequestUserLoginDTO(userObj.getLogin(), userObj.getPassword());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userObj, userObj.getPassword(), userObj.getAuthorities());
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Act
        User authenticatedUser = authenticationService.authenticateUserByLoginData(requestUserLoginDTO);

        // Asssert
        assertNotNull(authenticatedUser);
        assertEquals(requestUserLoginDTO.login(), authenticatedUser.getLogin());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }

    /**
     * Method under test: {@link AuthenticationService#authenticateUserByLoginData(RequestUserLoginDTO)}
     */

    @Test
    @DisplayName("Should throw exception when authentication fails")
    void testAuthenticateUserWithInvalidData() {
        // Arrange
        RequestUserLoginDTO invalidUser = UTestData.createInvalidUserLoginReq();

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUserByLoginData(invalidUser));
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }

    private void createAuthentication(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}