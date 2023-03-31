import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./main.css']
})
export class AppComponent {
  title = 'My Angular App';
  currentRoute: string = '';
}
