import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class GptService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getContent(prompt: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/v1/gpt/generate-content?content=${prompt}`, { responseType: 'text'});
  }
}
