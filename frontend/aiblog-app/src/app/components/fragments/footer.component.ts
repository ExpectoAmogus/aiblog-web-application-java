import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Event, NavigationEnd, Router} from "@angular/router";
import {ArticlesService} from "../../services/articles.service";
import {ArticleDTO} from "../../models/article";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {
  articles: ArticleDTO[] = [];
  constructor(
    private router: Router,
    private articlesService: ArticlesService
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
    this.articlesService.getArticles(0,4).subscribe({
      next: (articles) => {
          this.articles = articles;
      }
    })
  }
}
