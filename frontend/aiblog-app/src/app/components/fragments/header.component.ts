import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { createPopper } from '@popperjs/core';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent{

  @Input() title!: string;
  token = sessionStorage.getItem('token');

}
