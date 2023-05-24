import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {ImagesService} from '../../services/images.service';
import {forkJoin} from "rxjs";
import {CommentsDTO} from "../../models/comments";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  public currentUserId!: number;
  public article!: ArticleDTO;
  public images: string[] = [];
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
