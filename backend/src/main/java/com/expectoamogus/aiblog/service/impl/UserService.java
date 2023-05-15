package com.expectoamogus.aiblog.service.impl;

import com.expectoamogus.aiblog.dto.auth.ResponseDTO;
import com.expectoamogus.aiblog.dto.auth.UserAuthDTO;
import com.expectoamogus.aiblog.dto.auth.UserRegDTO;
import com.expectoamogus.aiblog.jwt.JwtService;
import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.models.enums.Role;
import com.expectoamogus.aiblog.service.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseDTO register(UserRegDTO request){
        var user = User.builder()
                .firstName(request.firstName())
                .email(request.username())
                .password(passwordEncoder.encode(request.password()))
                .isActive(true)
                .role(Role.ROLE_USER)
                .build();
        log.info("Saving new User with email: {}", user.getEmail());
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new ResponseDTO(user.getId(),"Bearer " + jwtToken, user.getRole().getAuthorities());
    }
    public ResponseDTO login(UserAuthDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        log.info("Auth User with email: {}", request.username());
        var user = userRepository.findByEmail(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
        var jwtToken = jwtService.generateToken(user);
        return new ResponseDTO(user.getId(),"Bearer " + jwtToken, user.getRole().getAuthorities());
    }
}
