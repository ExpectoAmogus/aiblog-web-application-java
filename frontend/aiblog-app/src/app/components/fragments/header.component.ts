import {Component, Input, OnInit} from '@angular/core';
import { Router, Event, NavigationEnd } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit{

  @Input() title!: string;
  token = sessionStorage.getItem('token');

  ngOnInit() {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.updateNavbar();
      }
    });
  }

  updateNavbar() {
    this.token = sessionStorage.getItem('token');
  }

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

    public logout(): void {
      this.authService.logout()
        this.router.navigate(['/login']);
    }

}
