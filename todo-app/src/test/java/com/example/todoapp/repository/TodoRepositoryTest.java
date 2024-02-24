package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {
    @Autowired
    private TodoRepository underTest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByUser() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");
        userRepository.save(theUser);

        Todo todo = new Todo();
        todo.setName("First Todo");
        todo.setStatus(80);
        todo.setColor("green-500");
        todo.setUser(theUser);
        underTest.save(todo);

        // when
        List<Todo> foundTodos = underTest.findByUser(theUser);

        // then
        assertThat(foundTodos).isEqualTo(List.of(todo));

    }
}