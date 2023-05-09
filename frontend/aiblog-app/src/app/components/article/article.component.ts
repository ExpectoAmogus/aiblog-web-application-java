import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {ImagesService} from '../../services/images.service';
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  public article!: ArticleDTO;
  public images: string[] = [];
  public articlesLatest: ArticleDTO[] = [];
  public articlesPopular: ArticleDTO[] = [];
  public articlesTrending: ArticleDTO[] = [];
  constructor(
    private route: ActivatedRoute,
    private articlesService: ArticlesService,
    private imagesService: ImagesService
  ) {
  }

  ngOnInit() {
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
    this.articlesService.getArticles().subscribe({
      next: (response) => {
        this.articlesLatest = response.slice(0, 6);
      }
    });
  }

  public getPopularArticles(): void {
    this.articlesService.getPopularArticles().subscribe({
      next: (response) => {
        this.articlesPopular = response.slice(0, 6);
      }
    });
  }

  public getTrendingArticles(): void {
    this.articlesService.getTrendingArticles().subscribe({
      next: (response) => {
        this.articlesTrending = response.slice(0, 6);
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
