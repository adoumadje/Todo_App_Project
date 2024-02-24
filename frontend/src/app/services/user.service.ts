import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private user = new BehaviorSubject<User | null>(null)

  constructor() { }

  public getUser = this.user.asObservable()

  public setUser(theUser:User | null) {
    this.user.next(theUser)
  }
}
