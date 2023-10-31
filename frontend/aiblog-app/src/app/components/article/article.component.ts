import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {ImagesService} from '../../services/images.service';
import {forkJoin} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {DomSanitizer, SafeUrl, Title} from '@angular/platform-browser';
import {AdminStatusService} from "../../services/admin-status.service";


@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  public currentUserId!: number;
  public article!: ArticleDTO;
  public images: SafeUrl[] = [];

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private articlesService: ArticlesService,
    private sanitizer: DomSanitizer,
    private imagesService: ImagesService,
    private titleService: Title,
  ) {
  }

  ngOnInit() {
    this.currentUserId = this.authService.getCurrentUserId();
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
          this.titleService.setTitle(`${this.article.title} - AI Blog`);
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
        this.images = responses.map((data) => {
          return this.sanitizer.bypassSecurityTrustUrl(data);
        });
      }
    });
  }

  public getImage(imageId: number): SafeUrl {
    return this.images[imageId];
  }
}
