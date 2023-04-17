import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap, catchError } from 'rxjs';
import { environment } from 'src/environments/environments';
import { ExceptionService } from './exception.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
  ) { }


  login(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/login`, { username: model.email, password: model.password })
      .pipe(
        tap(response => {
          if (response && response.token) {
            sessionStorage.setItem('token', response.token);
            sessionStorage.setItem('role', response.role)
          }
        })
    );
  }

  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('role');
    this.http.post<any>(`${this.apiUrl}/api/v1/auth/logout`, {});
  }

  register(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/register`, { firstName: model.firstName, username: model.email, password: model.password })
  
  }
}
