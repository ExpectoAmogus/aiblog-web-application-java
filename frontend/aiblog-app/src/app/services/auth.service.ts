import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  login(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/login`, { username: model.email, password: model.password });
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.http.post<any>(`${this.apiUrl}/api/v1/auth/logout`, {});
  }

  register(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/auth/register`, { firstName: model.firstName, username: model.email, password: model.password });
  }
}
