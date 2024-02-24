package com.example.todoapp.repository;

import com.example.todoapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @Test
    void shouldFindByEmail() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");
        theUser = underTest.save(theUser);

        // when
        User foundUser = underTest.findByEmail("fede.valverde@fmail.com");

        // then
        assertThat(foundUser).isEqualTo(theUser);
    }
}