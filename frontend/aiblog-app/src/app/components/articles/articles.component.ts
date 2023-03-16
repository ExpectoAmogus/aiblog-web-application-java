import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ArticlesService } from 'src/app/services/articles.service';
import { ArticleDTO } from '../../models/article';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  providers: [DatePipe]
})
export class ArticlesComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  public editArticle?: ArticleDTO;
  public deleteArticle?: ArticleDTO;

  constructor(private articlesService: ArticlesService) { } 

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

  // public onUpdateArticle(article: ArticleDTO): void {
  //   this.articlesService.updateArticle(article).subscribe({
  //     next: (response: ArticleDTO) => {
  //       console.log(response);
  //       this.getArticles();
  //     },
  //     error: (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   });
  // }

  // public onDeleteArticle(articleId: number): void {
  //   this.articlesService.deleteArticle(articleId).subscribe({
  //     next: (response: void) => {
  //       console.log(response);
  //       this.getArticles();
  //     },
  //     error: (error: HttpErrorResponse) => {
  //       alert(error.message);
  //     }
  //   });
  // }

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

  public onOpenModal(article: ArticleDTO, mode: string): void {
    const container = document.getElementById('section-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addArticleModal');
    }
    if (mode === 'edit') {
      this.editArticle = article;
      button.setAttribute('data-target', '#updateArticleModal');
    }
    if (mode === 'delete') {
      this.deleteArticle = article;
      button.setAttribute('data-target', '#deleteArticleModal');
    }
    container?.appendChild(button);
    button.click();
  }

}
