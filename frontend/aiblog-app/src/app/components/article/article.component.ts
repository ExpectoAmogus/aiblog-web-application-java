import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {ImagesService} from '../../services/images.service';
import {forkJoin} from "rxjs";
import {CommentsService} from "../../services/comments.service";
import {NgForm} from "@angular/forms";
import {CommentsDTO} from "../../models/comments";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  public currentUserId!: number;
  public comments: CommentsDTO[] = [];
  public article!: ArticleDTO;
  public images: string[] = [];
  public articlesLatest: ArticleDTO[] = [];
  public articlesPopular: ArticleDTO[] = [];
  public articlesTrending: ArticleDTO[] = [];
  public isAdmin: boolean | undefined;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private articlesService: ArticlesService,
    private imagesService: ImagesService
  ) {
  }

  ngOnInit() {
    this.currentUserId = this.authService.getCurrentUserId();
    this.isAdmin = this.authService.isAdmin();
    this.updateContent();

  }

  updateContent(): void {
    this.getArticle();
    this.getLatestArticles();
    this.getPopularArticles();
    this.getTrendingArticles();
  }

  public getArticle(): void {
    this.route.params.subscribe(params => {
      const articleId = parseInt(params['id'], 10);
      this.articlesService.getArticle(articleId).subscribe({
        next: (response: ArticleDTO) => {
          this.article = response;
          this.getImages(this.article.images);
        }
      });
      this.articlesService.incrementArticleViews(articleId).subscribe();
    });
  }

  public getLatestArticles(): void {
    this.articlesService.getArticles(0, 6).subscribe({
      next: (response) => {
        this.articlesLatest = response;
      }
    });
  }

  public getPopularArticles(): void {
    this.articlesService.getPopularArticles(0, 6).subscribe({
      next: (response) => {
        this.articlesPopular = response;
      }
    });
  }

  public getTrendingArticles(): void {
    this.articlesService.getTrendingArticles(0, 6).subscribe({
      next: (response) => {
        this.articlesTrending = response;
      }
    });
  }

  public getImages(imagesId: string[]): void {
    const requests = [];
    for (let i = 1; i <= imagesId.length; i++) {
      requests.push(this.imagesService.getImage(this.article.uuid, i));
    }
    forkJoin(requests).subscribe({
      next: (responses) => {
        this.images = responses;
      }
    });
  }

  public getImage(imageId: number): string {
    return this.images[imageId];
  }
}
