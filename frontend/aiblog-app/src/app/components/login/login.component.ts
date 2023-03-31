import {HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  model: any = {};
  sessionId: any = "";

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
  }

  public login(loginForm: NgForm): void {
    this.authService.login(loginForm.value).subscribe({
      next: (response) => {
        this.sessionId = response.sessionId;
        sessionStorage.setItem(
          'token',
          this.sessionId
        );
        this.router.navigate(['']);
        loginForm.reset()
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        loginForm.reset()
      }
    });
  }
}
