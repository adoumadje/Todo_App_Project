import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Todo } from '../interfaces/todo';
import { prodEnvironment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TodoService {
  baseUrl = prodEnvironment.apiBaseUrl

  constructor(private http:HttpClient) { }

  public createTodo(todoDetails:Todo) {
    return this.http.post(
      `${this.baseUrl}/create-todo`,
      todoDetails,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + sessionStorage.getItem('token')
        },
        responseType: 'json'
      }
    )
  }

  public getTodos(userEmail:string) {
    return this.http.get(
      `${this.baseUrl}/get-todos?userEmail=${userEmail}`,
      {
        headers: {
          'Authorization': 'Bearer ' + sessionStorage.getItem('token')
        },
        responseType: 'json'
      }
    )
  }

  public updateTodo(todoDetails:Todo) {
    return this.http.put(
      `${this.baseUrl}/edit-todo`,
      todoDetails,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + sessionStorage.getItem('token')
        },
        responseType: 'json'
      }
    )
  }

  public deleteTodo(todoId:number) {
    return this.http.delete(
      `${this.baseUrl}/delete-todo?todoId=${todoId}`,
      {
        headers: {
          'Authorization': 'Bearer ' + sessionStorage.getItem('token')
        },
        responseType: 'json'
      }
    )
  }
}
