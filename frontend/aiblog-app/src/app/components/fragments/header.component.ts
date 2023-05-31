import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Event, NavigationEnd, Router} from '@angular/router';
import {AuthService} from 'src/app/services/auth.service';
import {SearchService} from "../../services/search.service";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  public isAdmin: boolean | undefined;

  constructor(
    private router: Router,
    private authService: AuthService,
    private searchService: SearchService
  ) { }

  @Input() title!: string;
  @ViewChild('searchInput') searchInput!: ElementRef;

  token = sessionStorage.getItem('token');

  ngOnInit() {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.updateNavbar();
      }
    });
  }
  updateNavbar() {
    this.isAdmin = this.authService.isAdmin();
    this.token = sessionStorage.getItem('token');
    this.clearSearchQuery();
  }

  clearSearchQuery(): void {
    this.searchService.clearSearchQuery();
    this.searchInput.nativeElement.value = '';
  }

  public searchArticles(query: string){
    this.searchService.setSearchQuery(query);
  }
}
