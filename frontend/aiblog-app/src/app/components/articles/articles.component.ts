import {DatePipe} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {AuthService} from 'src/app/services/auth.service';
import {SearchService} from "../../services/search.service";
import {ImagesService} from "../../services/images.service";

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  providers: [DatePipe],
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  private originalArticles: ArticleDTO[] = [];
  public isAdmin: boolean | undefined;
  public articleImages: { [articleId: number]: string } = {};

  constructor(
    private articlesService: ArticlesService,
    private authService: AuthService,
    private router: Router,
    private searchService: SearchService,
    private imagesService: ImagesService
    ) { }

  ngOnInit() {
    this.isAdmin = this.authService.isAdmin();
    this.getArticles();

    this.searchService.getSearchQuery().subscribe((query) => {
      if (query) {
        this.searchArticles(query)
      } else {
        this.restoreArticles()
      }
    })
  }

  public getArticles(): void {
    this.articlesService.getArticles().subscribe({
      next: (response: ArticleDTO[]) => {
        this.articles = response;
        this.originalArticles = response;
        for (const article of response) {
          this.imagesService
            .getImage(article.uuid, 1)
            .subscribe(imageUrl => {
              this.articleImages[article.id] = imageUrl;
            });
        }
      }
    });
  }

  getArticleImage(articleId: number): string {
    return this.articleImages[articleId];
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

  public searchArticles(key: string): void {
    const results: ArticleDTO[] = [];
    for (const article of this.originalArticles) {
      if (article.title.toLowerCase().indexOf(key.toLowerCase()) !== -1
        || article.user.firstName.toLowerCase().indexOf(key.toLowerCase()) !== -1) {
        results.push(article);
      }
    }
    this.articles = results;
  }

  public restoreArticles(): void {
    this.articles = this.originalArticles;
  }

  truncateHTML(html: string, maxLength: number): string {
    let truncatedText = html.slice(0, maxLength);
    if (html.length > maxLength) {
      truncatedText += '...';
    }
    return truncatedText;
  }

}
