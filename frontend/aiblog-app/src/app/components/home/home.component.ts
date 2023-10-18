import {Component, OnInit} from '@angular/core';
import {ArticlesService} from "../../services/articles.service";
import {ArticleDTO} from "../../models/article";
import {ImagesService} from "../../services/images.service";
import {CATEGORIES} from "../../models/categories";
import {DomSanitizer} from "@angular/platform-browser";


@Component({
  selector: 'app-home-content',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  public trendingArticles: ArticleDTO[] = [];
  public articleImages: { [articleId: number]: string } = {};
  public categories = CATEGORIES;

  constructor(
    private articlesService: ArticlesService,
    private sanitizer: DomSanitizer,
    private imagesService: ImagesService
  ) {}

  ngOnInit() {
    this.getArticles();
    this.getTrendingArticles();
  }

  getArticles(): void {
    this.articlesService.getArticles(0,24).subscribe(articles => {
      this.articles = articles;
      for (const article of articles) {
        this.imagesService
          .getImage(article.uuid, 1)
          .subscribe((data) => {
            this.articleImages[article.id] = data;
          });
      }
    });
  }
  getTrendingArticles(): void {
    this.articlesService.getTrendingArticles(0,6).subscribe(articles => {
      this.trendingArticles = articles;
    });
  }

  getArticleImage(articleId: number): string {
    return this.articleImages[articleId];
  }

}
