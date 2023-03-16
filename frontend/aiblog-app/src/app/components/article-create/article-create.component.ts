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

  article: any = {};
  constructor(
    private articlesService: ArticlesService, 
    private router: Router,
    ) { } 


  ngOnInit(): void {
    this.article = new ArticleDTO();
  }

  getArticles(){
    this.router.navigate(['/articles'])
  }


  public onAddArticle(addForm: NgForm): void {
    this.articlesService.addArticle(addForm.value).subscribe({
      next: (response: ArticleDTO) => {
        console.log(response);
        this.getArticles();
        addForm.reset();
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
        addForm.reset();
      }
    }
    );
  }
}
