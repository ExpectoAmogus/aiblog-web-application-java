import {Pipe, PipeTransform} from '@angular/core';
import {ArticleDTO} from "./models/article";

@Pipe({
  name: 'filterByCategory'
})
export class FilterByCategoryPipe implements PipeTransform {

  transform(articles: ArticleDTO[], category: string): ArticleDTO[] {
    return articles.filter(article => article.category === category);
  }

}
