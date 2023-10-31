import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Event, NavigationEnd, Router} from '@angular/router';
import {AuthService} from 'src/app/services/auth.service';
import {SearchService} from "../../services/search.service";
import {AdminStatusService} from "../../services/admin-status.service";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  public isAdmin: boolean | undefined;

  constructor(
    private router: Router,
    public adminStatusService: AdminStatusService,
    private searchService: SearchService
  ) { }

  @Input() title!: string;
  @ViewChild('searchInput') searchInput!: ElementRef;

  token = sessionStorage.getItem('token');

  ngOnInit() {
    this.adminStatusService.initializeAdminStatus().then(() => {
      this.updateNavbar();
    });
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.updateNavbar();
      }
    });
  }
  updateNavbar() {
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
