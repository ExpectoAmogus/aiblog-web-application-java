import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {AuthService} from 'src/app/services/auth.service';
import {SearchService} from "../../services/search.service";
import {ImagesService} from "../../services/images.service";
import {CATEGORIES} from "../../models/categories";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  public articles: ArticleDTO[] = [];
  private originalArticles: ArticleDTO[] = [];
  public isAdmin: boolean | undefined;
  public articleImages: { [articleId: number]: SafeUrl } = {};
  public categories = CATEGORIES;
  public currentPage = 0;
  public pageSize = 24;
  public totalPages = 1;

  constructor(
    private articlesService: ArticlesService,
    private authService: AuthService,
    private router: Router,
    private searchService: SearchService,
    private imagesService: ImagesService,
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.isAdmin = this.authService.isAdmin();

    this.route.queryParamMap.subscribe((params) => {
      const category = params.get('category');
      if (category) {
        this.filterArticles(category);
      } else {
        this.getArticles()
      }
    });

    this.searchService.getSearchQuery().subscribe((query) => {
      if (query) {
        this.searchArticles(query)
      } else {
        this.restoreArticles()
      }
    })
  }

  public filterArticles(category?: string): void {
    this.getArticles(category);
    this.router.navigate(['/articles'], {queryParams: {category: category}});
  }

  public getArticles(category?: string): void {
      this.articlesService.getArticles(this.currentPage, this.pageSize).subscribe({
        next: (response) => {
          this.originalArticles = response;
          this.articles = category ? response.filter(article => article.category === category) : response;

          const totalArticles = this.articles.length;
          this.totalPages = Math.ceil(totalArticles / this.pageSize);
          if (this.totalPages < 1){
            this.totalPages = 1;
          }

          for (const article of response) {
            this.imagesService
              .getImage(article.uuid, 1)
              .subscribe((data) => {
                this.articleImages[article.id] = this.sanitizer.bypassSecurityTrustUrl(data);
              });
          }
        }
      });
  }


  getArticleImage(articleId: number): SafeUrl {
    return this.articleImages[articleId];
  }

  public updateArticle(article: ArticleDTO) {
    this.router.navigate(['/articles/article-edit', article.id]);
  }

  public deleteArticle(articleId: number): void {
    this.articlesService.deleteArticle(articleId).subscribe({
      next: () => {
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

  nextPage(): void {
    this.currentPage++;
    this.getArticles();
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.getArticles();
    }
  }
}
