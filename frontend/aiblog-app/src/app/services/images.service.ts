import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ExceptionService} from './exception.service';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
    ) {}

  getImage(articleId: string, imageId: number): Observable<string> {
    return this.http.get(`${this.apiUrl}/api/v1/images/${articleId}/${imageId}`, { responseType: 'text' })
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }
}
