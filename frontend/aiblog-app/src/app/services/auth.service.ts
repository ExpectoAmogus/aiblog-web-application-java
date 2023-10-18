import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ExceptionService} from './exception.service';
import {getAuthorities} from "../models/user";
import jwtDecode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
  ) { }

  isAdmin(): boolean {
    let authorities = this.getAuthorities();
    if (authorities === null){
      return false;
    }
    return authorities.some((authority: any) => authority === 'devs:write');
  }

  isAuthenticated(): boolean {
    // @ts-ignore
    let token = sessionStorage.getItem('token');
    return token !== null;
  }

  getAuthorities() {
    let token = sessionStorage.getItem('token');
    if (token !== null) {
      if (token.startsWith("Bearer ")) {
        const rawToken = token.substring(7);
        const decodedToken: any = jwtDecode(rawToken);
        return decodedToken.role.map((authority: any) => authority.authority);
      }
    }
  }
  getCurrentUserId(): number {
      // @ts-ignore
    return parseInt(sessionStorage.getItem('currentUserId'), 10);
  }

  login(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/login`, { username: model.email, password: model.password })
      .pipe(
        tap(response => {
          if (response && response.token) {
            sessionStorage.setItem('currentUserId', response.id);
            sessionStorage.setItem('token', response.token);
          }
        })
    );
  }

  logout(): void {
    sessionStorage.removeItem('currentUserId');
    sessionStorage.removeItem('token');
    this.http.post<any>(`${this.apiUrl}/api/v1/auth/logout`, {});
  }

  register(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/register`, { firstName: model.firstName, username: model.email, password: model.password })

  }
}
