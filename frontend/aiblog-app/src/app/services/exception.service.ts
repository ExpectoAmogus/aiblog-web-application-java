import {HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ExceptionService {

  constructor(private router: Router) { }

  handleHttpError(error: HttpErrorResponse): void {
    if (error.status === 403) {
      this.router.navigate(['/error', 403]);
    } else if (error.status === 404) {
      this.router.navigate(['/error', 404]);
    } else {
      this.router.navigate(['/error', 500]);
    }
  }
}
