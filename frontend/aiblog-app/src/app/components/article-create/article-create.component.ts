import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ArticleDTO} from 'src/app/models/article';
import {ArticlesService} from 'src/app/services/articles.service';
import {GptService} from 'src/app/services/gpt.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import {FormControl} from "@angular/forms";
import { CATEGORIES } from '../../models/categories';


@Component({
  selector: 'app-article-create',
  templateUrl: './article-create.component.html',
  styleUrls: ['./article-create.component.css']
})
export class ArticleCreateComponent implements OnInit {

  Editor = ClassicEditor as unknown as {
    create: any;
  };
  title: string = '';
  content: string = '';
  images!: FileList;
  prompt: string = '';
  categories = CATEGORIES;
  categoryControl = new FormControl('', []);
  selectedCategory: string = '';


  constructor(
    private articlesService: ArticlesService,
    private router: Router,
    private gptService: GptService
  ) { }


  ngOnInit(): void {
  }

  onCategorySelected(): void {
    //@ts-ignore
    this.selectedCategory = this.categoryControl.value;
  }

  getArticles() {
    this.router.navigate(['/articles'])
  }


  public onSubmit(): void {
    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('content', this.content);
    formData.append('category', this.selectedCategory);
    if (this.images) {
      for (let i = 0; i < this.images.length; i++) {
        formData.append('images', this.images[i]);
      }
    }

    this.articlesService.addArticle(formData).subscribe({
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

  generateContent() {
    this.gptService.getContent(this.prompt).subscribe({
      next: (response: any) => {
        this.content = response;
      }
    });
  }
}
