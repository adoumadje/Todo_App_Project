package com.example.todoapp.controller;

import com.example.todoapp.model.TodoModel;
import com.example.todoapp.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/create-todo")
    public TodoModel createTodo(@RequestBody TodoModel todoModel) {
        return todoService.createTodo(todoModel);
    }

    @GetMapping("/get-todos")
    public List<TodoModel> getTodos(@RequestParam("userEmail") String userEmail) {
        return todoService.getTodos(userEmail);
    }

    @PutMapping("/edit-todo")
    public TodoModel editTodo(@RequestBody TodoModel todoModel) {
        return todoService.editTodo(todoModel);
    }

    @DeleteMapping("/delete-todo")
    public Long deleteTodo(@RequestParam("todoId") Long todoId) {
        return todoService.deleteTodo(todoId);
    }
}
