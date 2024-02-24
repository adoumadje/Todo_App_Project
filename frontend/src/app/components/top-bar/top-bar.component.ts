import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService } from 'primeng/api';
import { User } from 'src/app/interfaces/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent implements OnInit {
  user!:User
  authenticated = false

  constructor(
    private confirmationService:ConfirmationService,
    private userService:UserService,
    private router:Router
  ) { }

  ngOnInit(): void {
    this.userService.getUser.subscribe(
      data => {
        if(data != null) {
          this.authenticated = true
          this.user = data as User
        } else if(sessionStorage.getItem('user')) {
          this.authenticated = true
          this.user = JSON.parse(sessionStorage.getItem('user') as string) as User
        }
      }
    )
  }



  askLogoutConfirmation() {
    this.confirmationService.confirm({
      accept: () => {
        this.authenticated = false
        sessionStorage.clear()
        this.userService.setUser(null)
        this.router.navigate(['login'])
      },
      reject: () => {
      }
    })
  }
}
