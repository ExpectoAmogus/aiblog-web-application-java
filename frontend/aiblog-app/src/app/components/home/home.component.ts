import {Component, OnInit} from '@angular/core';
import {ArticlesService} from "../../services/articles.service";
import {ArticleDTO} from "../../models/article";
import {ImagesService} from "../../services/images.service";

@Component({
  selector: 'app-home-content',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  public articleImages: { [articleId: number]: string } = {};

  constructor(
    private articlesService: ArticlesService,
    private imagesService: ImagesService
  ) {}

  ngOnInit() {
    this.getArticles();
  }

  getArticles(): void {
    this.articlesService.getArticles().subscribe(articles => {
      this.articles = articles.slice(0, 9);
      // for (const article of articles) {
      //   this.imagesService
      //     .getImage(article.uuid, 1)
      //     .subscribe(imageUrl => {
      //       this.articleImages[article.id] = imageUrl;
      //     });
      // }
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
