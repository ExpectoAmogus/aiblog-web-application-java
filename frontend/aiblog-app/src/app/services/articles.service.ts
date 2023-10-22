import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, map, Observable} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ArticleDTO} from '../models/article';
import {ExceptionService} from './exception.service';
import {Page} from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
  ) {
  }

  getArticles(title: string = '', page: number = 0, size: number = 6): Observable<Page> {
    let params = new HttpParams();
    params = params.append('title', title.toString());
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<Page>(`${this.apiUrl}/api/v1/articles/all`, {params})
  }

  getPopularArticles(title: string = '', page: number = 0, size: number = 6): Observable<Page> {
    let params = new HttpParams();
    params = params.append('title', title.toString());
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<Page>(`${this.apiUrl}/api/v1/articles/popular`, {params})
  }

  getTrendingArticles(title: string = '', page: number = 0, size: number = 6): Observable<Page> {
    let params = new HttpParams();
    params = params.append('title', title.toString());
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<Page>(`${this.apiUrl}/api/v1/articles/trending`, {params})
  }

  getArticle(articleId: number): Observable<ArticleDTO> {
    return this.http.get<ArticleDTO>(`${this.apiUrl}/api/v1/articles/find/${articleId}`)
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

  incrementArticleViews(id: number) {
    return this.http.post(`${this.apiUrl}/api/v1/articles/${id}/views`, {});
  }

}
