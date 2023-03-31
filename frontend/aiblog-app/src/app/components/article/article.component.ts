import {HttpErrorResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from 'src/app/services/articles.service';
import {ArticleDTO} from '../../models/article';
import {ImagesService} from '../../services/images.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  public article!: ArticleDTO;
  public images: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private articlesService: ArticlesService,
    private imagesService: ImagesService
  ) { }

  ngOnInit() {
    this.getArticle();
  }
  public getArticle(): void {
    const articleId = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.articlesService.getArticle(articleId).subscribe({
      next: (response: ArticleDTO) => {
        this.article = response;
        this.getImages(this.article.images);
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }
  public getImages(imagesId: string[]): void {
    for (let i = 1; i <= imagesId.length; i++) {
      this.imagesService.getImage(this.article.uuid, i).subscribe({
        next: (response: ArrayBuffer) => {
          const blob = new Blob([response]);
          const fileReader = new FileReader();
          fileReader.readAsDataURL(blob);
          fileReader.onload = () => {
            const url = fileReader.result as string;
            this.images.push(url);
          }
        },
        error: (error: HttpErrorResponse) => {
          alert(error.message);
        }
      });
    }
  }
}
