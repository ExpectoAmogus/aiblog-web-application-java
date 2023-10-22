import {Component, OnInit} from '@angular/core';
import {Event, NavigationEnd, Router} from "@angular/router";
import {ArticlesService} from "../../services/articles.service";
import {ArticleDTO} from "../../models/article";
import {ImagesService} from "../../services/images.service";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {CATEGORIES} from "../../models/categories";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {
  articles: ArticleDTO[] = [];
  articleImages: { [articleId: number]: SafeUrl } = {};
  public categories = CATEGORIES;
  constructor(
    private router: Router,
    private articlesService: ArticlesService,
    private imagesService: ImagesService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit() {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.updateFooter();
      }
    });
  }
  updateFooter() {
      this.getArticles();
  }

  getArticles() {
    this.articlesService.getArticles('',0,4).subscribe({
      next: (articles) => {
          this.articles = articles.content;

        for (const article of articles.content) {
          this.imagesService
            .getImage(article.uuid, 1)
            .subscribe((data) => {
              this.articleImages[article.id] = this.sanitizer.bypassSecurityTrustUrl(data);
            });
        }
      }
    })
  }

  getArticleImage(articleId: number): SafeUrl {
    return this.articleImages[articleId];
  }
}
