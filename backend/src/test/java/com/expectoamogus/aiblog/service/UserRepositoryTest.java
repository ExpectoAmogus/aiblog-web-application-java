package com.expectoamogus.aiblog.service;

import com.expectoamogus.aiblog.models.User;
import com.expectoamogus.aiblog.models.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userTest;

    @AfterEach
    void tearDown() {
        userTest.deleteAll();
    }

    @Test
    void findByEmail() {
        String email = "test@example.com";

        var user = User.builder()
                .firstName("test")
                .email(email)
                .password("test")
                .isActive(true)
                .role(Role.ROLE_USER)
                .build();
        userTest.save(user);

        assertThat(userTest.findByEmail(email)).isNotEmpty();
    }
}
