import {DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  providers: [DatePipe]
})
export class ArticlesComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  public isAdmin: boolean | undefined;

  constructor(
    private articlesService: ArticlesService,
    private authService: AuthService,
    private router: Router
    ) { }

  ngOnInit() {
    this.getArticles();
    this.isAdmin = this.authService.isAdmin();
  }

  public getArticles(): void {
    this.articlesService.getArticles().subscribe({
      next: (response: ArticleDTO[]) => {
        this.articles = response;
      }
    });
  }

  public updateArticle(article: ArticleDTO) {
    this.router.navigate(['/articles/article-edit', article.id]);
  }

  public deleteArticle(articleId: number): void {
    this.articlesService.deleteArticle(articleId).subscribe({
      next: (response) => {
        console.log(response);
        this.getArticles();
      }
    });
  }

  truncateHTML(html: string, maxLength: number): string {
    let truncatedText = html.slice(0, maxLength);
    if (html.length > maxLength) {
      truncatedText += '...';
    }
    return truncatedText;
  }

}
