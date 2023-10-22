import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {AuthService} from 'src/app/services/auth.service';
import {SearchService} from "../../services/search.service";
import {ImagesService} from "../../services/images.service";
import {CATEGORIES} from "../../models/categories";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {BehaviorSubject, map, Observable} from "rxjs";
import {Page} from "../../models/page";

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  public isAdmin: boolean | undefined;
  public articleImages: { [articleId: number]: SafeUrl } = {};
  public categories = CATEGORIES;
  public activeCategory?: string;
  public query?: string;

  articlesState$: Observable<Page> | undefined;
  // @ts-ignore
  responseSubject = new BehaviorSubject<Page>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();

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
        this.getArticles();
      }
    });

    this.searchService.getSearchQuery().subscribe((query) => {
      if (query) {
        this.gotToPage(query)
        this.query = query;
      } else {
        this.getArticles()
      }
    })
  }

  public filterArticles(category?: string): void {
    this.getArticles(category);
    this.router.navigate(['/articles'], {queryParams: {category: category}});
  }

  public getArticles(category?: string): void {
    this.articlesState$ = this.articlesService.getArticles().pipe(
      map((response) => {
        this.activeCategory = category ? category : 'All';
        console.log("Active category: ", this.activeCategory)
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.number);

        for (const article of response.content) {
          this.imagesService
            .getImage(article.uuid, 1)
            .subscribe((data) => {
              this.articleImages[article.id] = this.sanitizer.bypassSecurityTrustUrl(data);
            });
        }
        return response;
      })
    );
  }

  gotToPage(title?: string, pageNumber: number = 0): void {
    this.articlesState$ = this.articlesService.getArticles(title, pageNumber).pipe(
      map((response: Page) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(pageNumber);
        console.log(response);

        for (const article of response.content) {
          this.imagesService
            .getImage(article.uuid, 1)
            .subscribe((data) => {
              this.articleImages[article.id] = this.sanitizer.bypassSecurityTrustUrl(data);
            });
        }
        return response;
      })
    )
  }

  goToNextOrPreviousPage(direction?: string, title?: string): void {
    this.gotToPage(title, direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
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

}
