import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from './authentication.guard';
import {AboutComponent} from './components/about/about.component';
import {ArticleCreateComponent} from './components/article-create/article-create.component';
import {ArticleEditComponent} from './components/article-edit/article-edit.component';
import {ArticleComponent} from './components/article/article.component';
import {ArticlesComponent} from './components/articles/articles.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {ExceptionComponent} from './components/exception/exception.component';
import {ContactComponent} from "./components/contact/contact.component";

const routes: Routes = [
  { path: 'error/:errorCode', component: ExceptionComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  {
    path: 'articles',
    component: ArticlesComponent,
    canActivate: [AuthenticationGuard],
    data: { authorities: [{ authority: 'users:read' }] }
  },
  {
    path: 'articles/create',
    component: ArticleCreateComponent,
    canActivate: [AuthenticationGuard],
    data: { authorities: [{ authority: 'devs:read' }, { authority: 'devs:write' }] }
  },
  {
    path: 'articles/article/:id',
    component: ArticleComponent,
    canActivate: [AuthenticationGuard],
    data: { authorities: [{ authority: 'users:read' }] }
  },
  {
    path: 'articles/article-edit/:id',
    component: ArticleEditComponent,
    canActivate: [AuthenticationGuard],
    data: { authorities: [{ authority: 'devs:read' }, { authority: 'devs:write' }] }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
