import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Todo } from 'src/app/interfaces/todo';
import { User } from 'src/app/interfaces/user';
import { TodoService } from 'src/app/services/todo.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-todo-lister',
  templateUrl: './todo-lister.component.html',
  styleUrls: ['./todo-lister.component.css']
})
export class TodoListerComponent implements OnInit {
    fakeArr = []
    dialogVisible = false
    actionLabel = 'Create Todo'
    todoForm:any

    user!:User
    todos!:Todo[]
    todoId!:number

    constructor(
      private confirmationService:ConfirmationService,
      private fb:FormBuilder,
      private userService:UserService,
      private todoService:TodoService,
      private messageService:MessageService
    ) {
      this.todoForm = this.fb.group({
        name: ['', Validators.required],
        status: [0]
      })
    }


    ngOnInit(): void {
      this.userService.getUser.subscribe(
        data => {
          if(data != null) {
            this.user = data as User
          } else if(sessionStorage.getItem('user')) {
            this.user = JSON.parse(sessionStorage.getItem('user') as string) as User
          }
        }
      )
      this.todoService.getTodos(this.user.email).subscribe(
        response => {
          this.todos = response as Todo[]
        },
        error => {
          console.error(error.message)
        }
      )
    }

    get name() {
      return this.todoForm.controls['name']
    }

    get status() {
      return this.todoForm.controls['status']
    }

    askToDelete(todo:Todo) {
        this.confirmationService.confirm({
          header: 'Delete todo confirmation',
          message: `Are you sure you want to delete this todo: "${todo.name}"?`,
          accept: () => {
            this.todoService.deleteTodo(todo.id).subscribe(
              (response:any) => {
                this.todos = this.todos.filter(todo => todo.id != parseInt(response))
              },
              error => {
                this.messageService.add({
                  severity: 'error',
                  summary: 'Error',
                  detail: error.message
                })
              }
            )
          },
          reject: () => {

          }
        })
    }

    showAddForm() {
      this.dialogVisible = true
      this.actionLabel = 'Create Todo'
    }

    showUpdateForm(todo:Todo) {
      this.todoId = todo.id
      this.name.value = todo.name
      this.status.value = todo.status
      this.dialogVisible = true
      this.actionLabel = 'Update Todo'
    }

    cancel() {
      this.dialogVisible = false
      this.todoForm.reset()
    }

    submitDetails() {
      const postData = {...this.todoForm.value} as Todo
      postData.userEmail = this.user.email
      switch(this.actionLabel) {
        case 'Create Todo':
          this.todoService.createTodo(postData as Todo).subscribe(
            response => {
              this.todos.push(response as Todo)
            },
            error => {
              console.error(error)
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: error.message
              })
            }
          )
          break

        case 'Update Todo':
          postData.id = this.todoId
          this.todoService.updateTodo(postData as Todo).subscribe(
            (response:any) => {
              this.todos = this.todos.map(todo => {
                if(todo.id == response.id) {
                  return (response as Todo)
                } else {
                  return todo
                }
              })
            }, 
            error => {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: error.message
              })
            }
          )
      }
      this.dialogVisible = false
      this.todoForm.reset()
    }
}
