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

  public searchArticles(key: string): void {
    console.log(key);
    const results: ArticleDTO[] = [];
    for (const article of this.articles) {
      if (article.title.toLowerCase().indexOf(key.toLowerCase()) !== -1
        || article.user.firstName.toLowerCase().indexOf(key.toLowerCase()) !== -1) {
        results.push(article);
      }
    }
    this.articles = results;
    if (results.length === 0 || !key) {
      this.getArticles();
    }
  }

}
