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
  token: any = "";

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
  }

  public login(loginForm: NgForm): void {
    this.authService.login(loginForm.value).subscribe({
      next: (response) => {
        this.token = response.token;
        sessionStorage.setItem(
          'token',
          this.token
        );
        this.router.navigate(['']);
        loginForm.reset()
      },
      error: () => {
        loginForm.reset()
      }
    });
  }
}
