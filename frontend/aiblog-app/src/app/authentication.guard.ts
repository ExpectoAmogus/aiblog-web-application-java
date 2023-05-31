import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "./services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (state.url == "/login") {
      return true;
    }

    const authoritiesData = route.data['authorities'];
    let token = this.authService.isAuthenticated();
    let authorities = this.authService.getAuthorities();

    if (!token) {
      return this.router.parseUrl('/login');
    }
    if (!authorities.some((a: { authority: string; }) => authoritiesData.some((d: { authority: string; }) => d.authority === a.authority)) && token) {
      return this.router.navigate(['/error/403']);
    }
    return true;
  }
}
