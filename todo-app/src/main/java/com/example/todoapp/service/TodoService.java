package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.model.TodoModel;

import java.util.List;

public interface TodoService {
    TodoModel createTodo(TodoModel todoModel);

    List<TodoModel> getTodos(String userEmail);

    TodoModel editTodo(TodoModel todoModel);

    Long deleteTodo(Long todoId);
}
