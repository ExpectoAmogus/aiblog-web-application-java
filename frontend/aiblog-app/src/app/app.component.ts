import { Component } from '@angular/core';
import { ArticlesComponent } from './components/articles/articles.component';
import { ArticleDTO } from './models/article';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./main.css']
})
export class AppComponent {
  title = 'My Angular App';
  currentRoute: string = '';
}
