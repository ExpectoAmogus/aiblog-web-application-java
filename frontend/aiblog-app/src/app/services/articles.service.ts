import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environments';
import {ArticleDTO} from '../models/article';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getArticles(): Observable<ArticleDTO[]> {
    return this.http.get<ArticleDTO[]>(`${this.apiUrl}/api/v1/articles/all`);
  }

  getArticle(articleId: number): Observable<ArticleDTO> {
    return this.http.get<ArticleDTO>(`${this.apiUrl}/api/v1/articles/find/${articleId}`);
  }

  addArticle(article: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/v1/articles/add`, article);
  }

  updateArticle(article: any, articleId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/api/v1/articles/update/${articleId}`, article);
  }

  deleteArticle(articleId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/v1/articles/delete/${articleId}`);
  }
}
