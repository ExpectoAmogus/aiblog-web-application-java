import { Component, Input, OnInit } from '@angular/core';
import { Router, Event, NavigationEnd } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ArticleDTO } from 'src/app/models/article';
import { ArticlesService } from 'src/app/services/articles.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  
  constructor(
    private router: Router,
    private authService: AuthService,
    private articlesService: ArticlesService
  ) { }

  @Input() title!: string;
  token = sessionStorage.getItem('token');

  ngOnInit() {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.updateNavbar();
      }
    });
  }

  updateNavbar() {
    this.token = sessionStorage.getItem('token');
  }

  public logout(): void {
    this.authService.logout()
    this.router.navigate(['/login']);
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
}
