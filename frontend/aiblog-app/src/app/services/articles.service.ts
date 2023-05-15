import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, map, Observable} from 'rxjs';
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
  ) {
  }

  getArticles(page: number, size: number): Observable<ArticleDTO[]> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<ArticleDTO[]>(`${this.apiUrl}/api/v1/articles/all`, {params})
      .pipe(
        map(articles => {
          return articles.map(article => {
            const dateArray = article.dateOfCreated;
            // @ts-ignore
            const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5], dateArray[6] / 1000000);
            article.user.dateOfCreated = date.toISOString();
            article.dateOfCreated = date.toISOString();
            return article;
          });
        }),
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  getPopularArticles(page: number, size: number): Observable<ArticleDTO[]> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<ArticleDTO[]>(`${this.apiUrl}/api/v1/articles/popular`, {params})
      .pipe(
        map(articles => {
          return articles.map(article => {
            const dateArray = article.dateOfCreated;
            // @ts-ignore
            const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5], dateArray[6] / 1000000);
            article.user.dateOfCreated = date.toISOString();
            article.dateOfCreated = date.toISOString();
            return article;
          });
        }),
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  getTrendingArticles(page: number, size: number): Observable<ArticleDTO[]> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    return this.http.get<ArticleDTO[]>(`${this.apiUrl}/api/v1/articles/trending`, {params})
      .pipe(
        map(articles => {
          return articles.map(article => {
            const dateArray = article.dateOfCreated;
            // @ts-ignore
            const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5], dateArray[6] / 1000000);
            article.user.dateOfCreated = date.toISOString();
            article.dateOfCreated = date.toISOString();
            return article;
          });
        }),
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  getArticle(articleId: number): Observable<ArticleDTO> {
    return this.http.get<ArticleDTO>(`${this.apiUrl}/api/v1/articles/find/${articleId}`)
      .pipe(
        map(article => {
          const dateArray = article.dateOfCreated;
          // @ts-ignore
          const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5], dateArray[6] / 1000000);
          article.user.dateOfCreated = date.toISOString();
          article.dateOfCreated = date.toISOString();
          return article;
        }),
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

  incrementArticleViews(id: number) {
    return this.http.post(`${this.apiUrl}/api/v1/articles/${id}/views`, {});
  }

}
