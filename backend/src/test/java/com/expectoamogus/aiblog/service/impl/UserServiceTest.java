package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.auth.ResponseDTO;
import com.expectoamogus.aiblog.dto.auth.UserAuthDTO;
import com.expectoamogus.aiblog.dto.auth.UserRegDTO;
import com.expectoamogus.aiblog.jwt.JwtService;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.models.enums.Role;
import com.expectoamogus.aiblog.service.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    UserService userService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void register() {
        UserRegDTO userRegDTO = new UserRegDTO(
                "testname",
                "testusername@gmail.com",
                "testpassword"
        );
        ResponseDTO created = userService.register(userRegDTO);

        assertThat(created.username()).isSameAs(userRegDTO.username());
    }

    @Test
    void login() {
        UserAuthDTO userAuthDTO = new UserAuthDTO(
                "testusername@gmail.com",
                "testpassword"
        );
        User existingUser = User.builder()
                .email(userAuthDTO.username())
                .password("testpassword")
                .isActive(true)
                .role(Role.ROLE_USER)
                .build();

        given(userRepository.findByEmail(userAuthDTO.username())).willReturn(Optional.of(existingUser));

        ResponseDTO auth = userService.login(userAuthDTO);

        assertThat(auth.username()).isSameAs(userAuthDTO.username());
        assertThat(auth.token()).startsWith("Bearer ");
    }
}