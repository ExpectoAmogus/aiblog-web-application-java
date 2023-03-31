import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from './authentication.guard';
import {AboutComponent} from './components/about/about.component';
import {ArticleCreateComponent} from './components/article-create/article-create.component';
import { ArticleEditComponent } from './components/article-edit/article-edit.component';
import {ArticleComponent} from './components/article/article.component';
import {ArticlesComponent} from './components/articles/articles.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  {
    path: '', canActivate: [AuthenticationGuard], children: [
      { path: '', component: HomeComponent },
      { path: 'login', component: LoginComponent },
      { path: 'about', component: AboutComponent },
      { path: 'articles', component: ArticlesComponent },
      { path: 'articles/create', component: ArticleCreateComponent },
      { path: 'articles/article/:id', component: ArticleComponent },
      { path: 'articles/article-edit/:id', component: ArticleEditComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
