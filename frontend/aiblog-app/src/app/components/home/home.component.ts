import {Component, OnInit} from '@angular/core';
import {ArticlesService} from "../../services/articles.service";
import {ArticleDTO} from "../../models/article";
import {ImagesService} from "../../services/images.service";
import {CATEGORIES} from "../../models/categories";
import { FilterByCategoryPipe } from '../../filter-by-category.pipe';


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
    private imagesService: ImagesService
  ) {}

  ngOnInit() {
    this.getArticles();
    this.getTrendingArticles();
  }

  getArticles(): void {
    this.articlesService.getArticles().subscribe(articles => {
      this.articles = articles.slice(0, 24);
      for (const article of articles) {
        this.imagesService
          .getImage(article.uuid, 1)
          .subscribe(imageUrl => {
            this.articleImages[article.id] = imageUrl;
          });
      }
    });
  }
  getTrendingArticles(): void {
    this.articlesService.getTrendingArticles().subscribe(articles => {
      this.trendingArticles = articles.slice(0, 6);
    });
  }

  getArticleImage(articleId: number): string {
    return this.articleImages[articleId];
  }

  truncateHTML(html: string, maxLength: number): string {
    let truncatedText = html.slice(0, maxLength);
    if (html.length > maxLength) {
      truncatedText += '...';
    }
    return truncatedText;
  }

}
