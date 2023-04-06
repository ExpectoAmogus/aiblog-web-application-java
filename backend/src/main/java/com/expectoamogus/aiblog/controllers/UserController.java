package com.expectoamogus.aiblog.controllers;

import com.expectoamogus.aiblog.dto.auth.ResponseDTO;
import com.expectoamogus.aiblog.dto.auth.UserAuthDTO;
import com.expectoamogus.aiblog.dto.auth.UserRegDTO;
import com.expectoamogus.aiblog.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserAuthDTO request) {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserRegDTO request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }
}
