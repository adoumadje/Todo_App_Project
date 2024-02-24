package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.model.TodoModel;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public TodoServiceImpl(TodoRepository todoRepository,
                           UserRepository userRepository,
                           UtilsService utilsService) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public TodoModel createTodo(TodoModel todoModel) {
        User user = userRepository.findByEmail(todoModel.getUserEmail());
        if(user == null) {
            throw new EntityNotFoundException("Forbidden");
        }
        Todo todo = new Todo();
        todo.setName(todoModel.getName());
        todo.setStatus(todoModel.getStatus());
        todo.setColor(utilsService.generateTodoColor(todoModel.getStatus()));
        todoModel.setColor(todo.getColor());
        todo.setUser(user);
        todo = todoRepository.save(todo);
        todoModel.setId(todo.getId());
        return todoModel;
    }

    @Override
    public List<TodoModel> getTodos(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null) {
            throw new EntityNotFoundException("Forbidden");
        }
        List<Todo> todos = todoRepository.findByUser(user);
        List<TodoModel> todoModels = new ArrayList<>();
        todos.forEach(todo -> {
            TodoModel todoModel = new TodoModel();
            todoModel.setId(todo.getId());
            todoModel.setName(todo.getName());
            todoModel.setStatus(todo.getStatus());
            todoModel.setColor(todo.getColor());
            todoModel.setUserEmail(userEmail);
            todoModels.add(todoModel);
        });
        return todoModels;
    }

    @Override
    public TodoModel editTodo(TodoModel todoModel) {
        Optional<Todo> todo = todoRepository.findById(todoModel.getId());
        if(todo.isEmpty()) {
            throw new EntityNotFoundException("Todo does not exist");
        }
        todo.ifPresent(todo1 -> {
            todo1.setName(todoModel.getName());
            todo1.setStatus(todoModel.getStatus());
            todo1.setColor(utilsService.generateTodoColor(todoModel.getStatus()));
            todoModel.setColor(todo1.getColor());
            todoRepository.save(todo1);
        });
        return todoModel;
    }

    @Override
    public Long deleteTodo(Long todoId) {
        Optional<Todo> todo = todoRepository.findById(todoId);
        if(todo.isEmpty()) {
            throw new EntityNotFoundException("Forbidden");
        }
        todo.ifPresent(todoRepository::delete);
        return todoId;
    }
}
