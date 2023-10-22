import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ArticleDTO} from "../../models/article";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ArticlesService} from "../../services/articles.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit{

  public articlesLatest: ArticleDTO[] = [];
  public articlesPopular: ArticleDTO[] = [];
  public articlesTrending: ArticleDTO[] = [];

  @Input() categories!: {viewValue: string, value: string}[];
  @Output() filterArticles = new EventEmitter<string>();

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private articlesService: ArticlesService
  ) {
  }

  ngOnInit() {
    this.updateContent();

  }

  updateContent(): void {
    this.getLatestArticles();
    this.getPopularArticles();
    this.getTrendingArticles();
  }

  public getLatestArticles(): void {
    this.articlesService.getArticles('', 0,6).subscribe({
      next: (response) => {
        this.articlesLatest = response.content;
      }
    });
  }

  public getPopularArticles(): void {
    this.articlesService.getPopularArticles('', 0,6).subscribe({
      next: (response) => {
        this.articlesPopular = response.content;
      }
    });
  }

  public getTrendingArticles(): void {
    this.articlesService.getTrendingArticles('', 0,6).subscribe({
      next: (response) => {
        this.articlesTrending = response.content;
      }
    });
  }
}
