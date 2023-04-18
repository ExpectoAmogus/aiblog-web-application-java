import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ExceptionService} from './exception.service';

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
    // @ts-ignore
    let authorities = JSON.parse(sessionStorage.getItem('authorities'));
    if (authorities === null){
      return false;
    }
    return authorities.some((a: { authority: string; }) => a.authority === 'devs:write');
  }
  login(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/login`, { username: model.email, password: model.password })
      .pipe(
        tap(response => {
          if (response && response.token) {
            sessionStorage.setItem('token', response.token);
            sessionStorage.setItem('authorities', JSON.stringify(response.authorities))
          }
        })
    );
  }

  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('authorities');
    this.http.post<any>(`${this.apiUrl}/api/v1/auth/logout`, {});
  }

  register(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/register`, { firstName: model.firstName, username: model.email, password: model.password })

  }
}
