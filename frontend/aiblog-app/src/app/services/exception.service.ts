import {HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ExceptionService {

  constructor(private router: Router) { }

  handleHttpError(error: HttpErrorResponse): void {
      this.router.navigate(['/error', error.status]);
    }
}
