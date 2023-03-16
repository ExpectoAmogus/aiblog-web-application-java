package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.ResponseDTO;
import com.expectoamogus.aiblog.dto.UserAuthDTO;
import com.expectoamogus.aiblog.dto.UserDTO;
import com.expectoamogus.aiblog.dto.UserRegDTO;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.service.impl.UserService;
import com.expectoamogus.aiblog.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserAuthDTO user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.username(), user.password())
        );
        final String sessionId = sessionRegistry.registerSession(user.username());
        ResponseDTO responseDTO = new ResponseDTO(sessionId);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegDTO userDto) {
        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setEmail(userDto.username());
        user.setPassword(userDto.password());
        userService.createUser(user);
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getRole().getAuthorities(),
                user.getUsername(),
                null,
                user.getDateOfCreated());
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }
}
