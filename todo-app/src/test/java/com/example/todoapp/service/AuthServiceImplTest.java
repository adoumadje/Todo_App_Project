package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private UtilsService utilsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    private AuthService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthServiceImpl(userRepository, tokenService, utilsService, passwordEncoder);
    }

    @Test
    void shouldEncodePasswordWhenRegister() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        // when
        underTest.register(theUser);

        // then
        verify(passwordEncoder).encode("Password1234#");
    }

    @Test
    void isSavedUserSameAsPassedUser() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        // when
        underTest.register(theUser);

        // then
        ArgumentCaptor<User> userArgumentCaptor =ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getFullname()).isEqualTo(theUser.getFullname());
        assertThat(capturedUser.getEmail()).isEqualTo(theUser.getEmail());
    }

    @Test
    void shouldThrowEmailAlreadyExistException() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        given(userRepository.findByEmail(theUser.getEmail()))
                .willReturn(theUser);

        // when
        // then
        assertThatThrownBy(() -> underTest.register(theUser))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User already exist");
    }

    @Test
    void forForgotPasswordShouldReturnTokenMap() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> forgotter = new HashMap<>();
        forgotter.put("email", "fede.valverde@fmail.com");

        given(userRepository.findByEmail(forgotter.get("email")))
                .willReturn(theUser);

        given(tokenService.generateToken(authentication))
                .willReturn("xyz");

        // when
        Map<String, String> map = underTest.forgotPassword(authentication, forgotter);

        // then
        assertThat(map).isNotNull();
    }

    @Test
    void forForgotPasswordShouldThrowBadRequest() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> forgotter = new HashMap<>();

        // when
        // then
        assertThatThrownBy(() -> underTest.forgotPassword(authentication, forgotter))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad Request: misses some fields");
    }

    @Test
    void forForgotPasswordShouldThrowUserNotExist() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> forgotter = new HashMap<>();
        forgotter.put("email", "fede.valverde@fmail.com");

        given(userRepository.findByEmail(forgotter.get("email")))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.forgotPassword(authentication, forgotter))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User does not exist");
    }

    @Test
    void shouldResetPassword() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234");

        Map<String, String> resetModel = new HashMap<>();
        resetModel.put("email", "fede.valverde@fmail.com");
        resetModel.put("newPass", "Password1234#");
        resetModel.put("confirmPass", "Password1234#");

        given(userRepository.findByEmail(resetModel.get("email")))
                .willReturn(theUser);

        // when
        underTest.resetPassword(resetModel);

        // then
        verify(userRepository).save(theUser);

    }

    @Test
    void forResetPasswordShouldThrowBadRequest() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234");

        Map<String, String> resetModel = new HashMap<>();
        resetModel.put("email", "fede.valverde@fmail.com");
        resetModel.put("newPass", "Password1234#");

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(resetModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad request: missing fields");
    }

    @Test
    void forResetPasswordShouldThrowPasswordMismatch() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234");

        Map<String, String> resetModel = new HashMap<>();
        resetModel.put("email", "fede.valverde@fmail.com");
        resetModel.put("newPass", "Password1234#");
        resetModel.put("confirmPass", "Password1234#7");

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(resetModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Passwords mismatch");
    }

    @Test
    void forResetPasswordShouldEntityNotFound() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234");

        Map<String, String> resetModel = new HashMap<>();
        resetModel.put("email", "fede.valverde@fmail.com");
        resetModel.put("newPass", "Password1234#");
        resetModel.put("confirmPass", "Password1234#");

        given(userRepository.findByEmail(resetModel.get("email")))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.resetPassword(resetModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User does not exist");
    }

    @Test
    void shouldLogin() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> loginModel = new HashMap<>();
        loginModel.put("email", "fede.valverde@fmail.com");
        loginModel.put("password", "Password1234#");

        given(userRepository.findByEmail(loginModel.get("email")))
                .willReturn(theUser);

        given(passwordEncoder.matches(loginModel.get("password"), "Password1234#"))
                .willReturn(true);

        given(tokenService.generateToken(authentication))
                .willReturn("xyz");

        // when
        var result = underTest.login(authentication, loginModel);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowBadRequest() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> loginModel = new HashMap<>();
        loginModel.put("email", "fede.valverde@fmail.com");

        // when
        // then
        assertThatThrownBy(() -> underTest.login(authentication, loginModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad request");
    }

    @Test
    void shouldThrowUserNotExist() {
        // given
        Map<String, String> loginModel = new HashMap<>();
        loginModel.put("email", "fede.valverde@fmail.com");
        loginModel.put("password", "Password1234#");

        given(userRepository.findByEmail(loginModel.get("email")))
                .willReturn(null);

        // when
        // then
        assertThatThrownBy(() -> underTest.login(authentication, loginModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User does not exist");
    }

    @Test
    void shouldThrowInvalidPassword() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Map<String, String> loginModel = new HashMap<>();
        loginModel.put("email", "fede.valverde@fmail.com");
        loginModel.put("password", "Password1234#");

        given(userRepository.findByEmail(loginModel.get("email")))
                .willReturn(theUser);

        given(passwordEncoder.matches(loginModel.get("password"), "Password1234#"))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.login(authentication, loginModel))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid password");
    }
}