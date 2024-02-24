package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.model.TodoModel;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UtilsService utilsService;
    private TodoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoServiceImpl(todoRepository, userRepository, utilsService);
    }

    @Test
    void shouldCreateTodo() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        TodoModel todoModel = new TodoModel();
        todoModel.setName("First Todo");
        todoModel.setStatus(80);
        todoModel.setUserEmail("fede.valverde@fmail.com");

        Todo todo = new Todo();
        todo.setName("First Todo");
        todo.setStatus(80);
        todo.setColor("green-500");
        todo.setUser(theUser);

        given(userRepository.findByEmail(todoModel.getUserEmail()))
                .willReturn(theUser);

        given(utilsService.generateTodoColor(todoModel.getStatus()))
                .willReturn("green-500");

        given(todoRepository.save(any()))
                .willReturn(todo);

        // when
        // then
        assertThat(underTest.createTodo(todoModel)).isNotNull();
    }

    @Test
    void forCreateTodoShouldThrowForbidden() {
        // given
        TodoModel todoModel = new TodoModel();

        given(userRepository.findByEmail(todoModel.getUserEmail()))
                .willReturn(null);
        // when
        // then
        assertThatThrownBy(() -> underTest.createTodo(todoModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Forbidden");
    }

    @Test
    void shouldGetTodos() {
        // given
        String userEmail = "fede.valverde@fmail.com";

        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        Todo todo = new Todo();
        todo.setName("First Todo");
        todo.setStatus(80);
        todo.setColor("green-500");
        todo.setUser(theUser);

        given(userRepository.findByEmail(userEmail))
                .willReturn(theUser);

        given(todoRepository.findByUser(any()))
                .willReturn(List.of(todo));

        // when
        // then
        assertThat(underTest.getTodos(userEmail)).isNotNull();
    }

    @Test
    void forGetTodosShouldThrowForbidden() {
        // given
        String userEmail = "fede.valverde@fmail.com";

        given(userRepository.findByEmail(userEmail))
                .willReturn(null);
        // when
        // then
        assertThatThrownBy(() -> underTest.getTodos(userEmail))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Forbidden");
    }

    @Test
    void shouldEditTodo() {
        // given
        User theUser = new User();
        theUser.setFullname("Fede Valverde");
        theUser.setEmail("fede.valverde@fmail.com");
        theUser.setPassword("Password1234#");

        TodoModel todoModel = new TodoModel();
        todoModel.setId(1L);
        todoModel.setName("First Todo");
        todoModel.setStatus(80);
        todoModel.setUserEmail("fede.valverde@fmail.com");

        Todo todo = new Todo();
        todo.setName("First Todo");
        todo.setStatus(80);
        todo.setColor("green-500");
        todo.setUser(theUser);

        given(todoRepository.findById(todoModel.getId()))
                .willReturn(Optional.of(todo));

        // when
        // then
        assertThat(underTest.editTodo(todoModel)).isNotNull();
    }

    @Test
    void forEditTodoShouldThrowForbidden() {
        // given
        TodoModel todoModel = new TodoModel();

        given(todoRepository.findById(todoModel.getId()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.editTodo(todoModel))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Todo does not exist");
    }

    @Test
    void shouldDeleteTodo() {
        // given
        Long todoId = 1L;

        Todo todo = new Todo();

        given(todoRepository.findById(todoId))
                .willReturn(Optional.of(todo));

        // when
        // then
        assertThat(underTest.deleteTodo(todoId)).isNotNull();
    }

    @Test
    void forDeleteTodoShouldThrowForbidden() {
        // given
        Long todoId = 1L;

        given(todoRepository.findById(todoId))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteTodo(todoId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Forbidden");
    }
}