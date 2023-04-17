import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AboutComponent } from './components/about/about.component';
import { ArticleComponent } from './components/article/article.component';
import { ArticlesComponent } from './components/articles/articles.component';
import { FooterComponent } from './components/fragments/footer.component';
import { HeaderComponent } from './components/fragments/header.component';
import { ArticleCreateComponent } from './components/article-create/article-create.component';
import { LoginComponent } from './components/login/login.component';
import { RequestInterceptor } from './request.interceptor';
import { ArticleEditComponent } from './components/article-edit/article-edit.component';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { ExceptionComponent } from './components/exception/exception.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    AboutComponent,
    FooterComponent,
    ArticlesComponent,
    ArticleComponent,
    ArticleCreateComponent,
    LoginComponent,
    ArticleEditComponent,
    ExceptionComponent,
  ],
  imports: [
    CKEditorModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
