import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleDTO} from 'src/app/models/article';
import {ArticlesService} from 'src/app/services/articles.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';

@Component({
  selector: 'app-article-edit',
  templateUrl: './article-edit.component.html',
  styleUrls: ['./article-edit.component.css']
})
export class ArticleEditComponent implements OnInit {
  Editor = ClassicEditor as unknown as {
    create: any;
  };
  article!: ArticleDTO;
  images!: FileList;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articlesService: ArticlesService,
  ) { }

  ngOnInit(): void {
    const articleId = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.articlesService.getArticle(articleId).subscribe({
      next: (response: ArticleDTO) => {
        console.log(response);
        this.article = response;
      }
    });
  }
  getArticles() {
    this.router.navigate(['/articles'])
  }

  saveArticle() {
    const formData = new FormData();
    formData.append('title', this.article.title);
    formData.append('content', this.article.content);
    if (this.images && this.images.length > 0) {
      for (let i = 0; i < this.images.length; i++) {
        formData.append('images', this.images[i]);
      }
    }

    this.articlesService.updateArticle(formData, this.article.id).subscribe({
      next: (response: ArticleDTO) => {
        console.log(response);
        this.getArticles();
      }
    });
  }

  onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      this.images = target.files;
    }
  }
}
