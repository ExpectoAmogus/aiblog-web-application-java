import {DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  providers: [DatePipe]
})
export class ArticlesComponent implements OnInit {
  public articles: ArticleDTO[] = [];

  constructor(
    private articlesService: ArticlesService,
    private router: Router
    ) { }

  ngOnInit() {
    this.getArticles();
  }
  public getArticles(): void {
    this.articlesService.getArticles().subscribe({
      next: (response: ArticleDTO[]) => {
        this.articles = response;
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
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
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
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
