import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ArticleDTO } from 'src/app/models/article';
import { ArticlesService } from 'src/app/services/articles.service';

@Component({
  selector: 'app-article-create',
  templateUrl: './article-create.component.html',
  styleUrls: ['./article-create.component.css']
})
export class ArticleCreateComponent implements OnInit {

  title: string = '';
  content: string = '';
  images!: FileList;

  constructor(
    private articlesService: ArticlesService,
    private router: Router,
  ) { }


  ngOnInit(): void {
  }

  getArticles() {
    this.router.navigate(['/articles'])
  }


  public onSubmit(): void {
    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('content', this.content);
    if (this.images) {
      for (let i = 0; i < this.images.length; i++) {
        formData.append('images', this.images[i]);
      }
    }

    this.articlesService.addArticle(formData).subscribe({
      next: (response: ArticleDTO) => {
        console.log(response);
        this.getArticles();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
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