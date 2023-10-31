import {Component, OnInit} from '@angular/core';
import {ArticlesService} from "../../services/articles.service";
import {CATEGORIES} from "../../models/categories";
import {ArticleDTO} from "../../models/article";
import {Router} from "@angular/router";

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit{
  articles: ArticleDTO[] = [];
  categories = CATEGORIES;
  selectedCategory: string = '';

  constructor(private articlesService: ArticlesService,
              private router: Router,) { }

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.articlesService.getArticles('', this.selectedCategory).subscribe(articles => {
      this.articles = articles.content;
    });
  }

  public updateArticle(article: ArticleDTO) {
    this.router.navigate(['/admin/article/edit', article.id]);
  }

  public deleteArticle(articleId: number): void {
    this.articlesService.deleteArticle(articleId).subscribe({
      next: () => {
        this.loadArticles();
      }
    });
  }

  onCategorySelected(): void {
    this.loadArticles();
  }
}
