import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationModel } from '../interfaces/registration-model';
import { ForgotModel } from '../interfaces/forgot-model';
import { ResetModel } from '../interfaces/reset-model';
import { LoginModel } from '../interfaces/login-model';
import { prodEnvironment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  baseUrl = prodEnvironment.apiBaseUrl
  basicUsername = prodEnvironment.basicUsername
  basicPassword = prodEnvironment.basicPassword


  constructor(private http:HttpClient) {}

  public registerUser(userDetails:RegistrationModel) {
    return this.http.post(
      `${this.baseUrl}/register`,
      userDetails,
      { responseType: 'text' }
    )
  }

  public forgotPassword(userDetails:ForgotModel) {
    return this.http.post(
      `${this.baseUrl}/forgot-password`,
      userDetails,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + btoa(this.basicUsername + ':' + this.basicPassword)
        },
        responseType: 'json'
      }
    )
  }

  public resetPassword(userDetails:ResetModel) {
    return this.http.put(
      `${this.baseUrl}/reset-password`,
      userDetails,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + sessionStorage.getItem('reset_token')
        },
        responseType: 'text'
      }
    )
  }

  public login(userDetails:LoginModel) {
    return this.http.post(
      `${this.baseUrl}/login`,
      userDetails,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Basic ' + btoa(this.basicUsername + ':' + this.basicPassword)
        },
        responseType: 'json'
      }
    )
  }
}
