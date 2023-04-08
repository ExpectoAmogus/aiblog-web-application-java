import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpXsrfTokenExtractor} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

  constructor(private tokenExtractor: HttpXsrfTokenExtractor) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let token = sessionStorage.getItem('token');
    if (token) {
      request = request.clone({ headers: request.headers.set('Authorization', token) });
    }
    if (request.method !== 'GET') {
      const csrf = this.tokenExtractor.getToken() as string;
      if (csrf) {
        request = request.clone({ headers: request.headers.set('X-XSRF-TOKEN', csrf) });
        request = request.clone({ withCredentials: true });
      }
    }
    return next.handle(request);
  }
}
