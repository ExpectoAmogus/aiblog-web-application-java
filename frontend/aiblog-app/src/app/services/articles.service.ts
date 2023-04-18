import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ArticleDTO} from '../models/article';
import {ExceptionService} from './exception.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
    ) {}

  getArticles(): Observable<ArticleDTO[]> {
    return this.http.get<ArticleDTO[]>(`${this.apiUrl}/api/v1/articles/all`)
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }

  getArticle(articleId: number): Observable<ArticleDTO> {
    return this.http.get<ArticleDTO>(`${this.apiUrl}/api/v1/articles/find/${articleId}`)
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }

  addArticle(article: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/articles/add`, article)
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }

  updateArticle(article: any, articleId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/api/v1/articles/update/${articleId}`, article)
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }

  deleteArticle(articleId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/v1/articles/delete/${articleId}`)
    .pipe(
      catchError(error => {
        this.errorHandlingService.handleHttpError(error);
        throw error;
      })
    );
  }
}
