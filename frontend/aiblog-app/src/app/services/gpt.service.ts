import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from 'src/environments/environments';
import { ExceptionService } from './exception.service';

@Injectable({
  providedIn: 'root'
})
export class GptService {

  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
    ) {}

  getContent(prompt: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/v1/gpt/generate-content?content=${prompt}`, { responseType: 'text'})
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }
}
