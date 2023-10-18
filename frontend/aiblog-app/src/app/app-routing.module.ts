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
import {AppLayoutComponent} from "./components/app-layout/app-layout.component";
import {LogoutComponent} from "./components/logout/logout.component";
import {AdminPanelComponent} from "./components/admin-panel/admin-panel.component";

const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login',
      intendedUrl: 'articles'
    }
  },
  {
    path: 'register',
    component: RegisterComponent,
    data: {title: 'Register'}
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: 'error/:errorCode',
    component: ExceptionComponent,
    data: {title: 'Something went wrong!'}
  },
  {
    path: '',
    component: AppLayoutComponent,
    children: [
      {
        path: '',
        component: HomeComponent,
        data: {title: 'Home'}
      },
      {
        path: 'admin/panel',
        component: AdminPanelComponent,
        data: {
          authorities: ['devs:read', 'devs:write'],
          title: 'Admin Panel'
        }
      },
      {
        path: 'about',
        component: AboutComponent,
        data: {title: 'About Us'}
      },
      {
        path: 'contact',
        component: ContactComponent,
        data: {title: 'Contact Us'}
      },
      {
        path: 'articles',
        component: ArticlesComponent,
        canActivate: [AuthenticationGuard],
        data: {
          authorities: ['users:read'],
          title: 'Articles'
        }
      },
      {
        path: 'articles/create',
        component: ArticleCreateComponent,
        canActivate: [AuthenticationGuard],
        data: {
          authorities: ['devs:read','devs:write'],
          title: 'Create Article'
        }
      },
      {
        path: 'articles/article/:id',
        component: ArticleComponent,
        canActivate: [AuthenticationGuard],
        data: {
          authorities: ['users:read'],
          title: 'Article'
        }
      },
      {
        path: 'articles/article-edit/:id',
        component: ArticleEditComponent,
        canActivate: [AuthenticationGuard],
        data: {
          authorities: ['devs:read', 'devs:write'],
          title: 'Edit Article'
        }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled', anchorScrolling: 'enabled' })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
