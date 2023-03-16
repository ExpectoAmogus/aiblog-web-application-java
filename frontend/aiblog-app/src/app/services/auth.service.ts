import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  login(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/login`, { username: model.email, password: model.password });
  }

  register(model: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/register`, { firstName: model.firstName, username: model.email, password: model.password });
  }
}
