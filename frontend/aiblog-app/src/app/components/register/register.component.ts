import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [FormsModule],
  standalone: true
})
export class RegisterComponent implements OnInit{ 
  model: any = {};

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {}

  public register(regForm: NgForm): void {
    this.authService.register(regForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.router.navigate(['/login']);
        regForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        regForm.reset();
      }
    }
    );
  }
}
