import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-exception',
  templateUrl: './exception.component.html',
  styleUrls: ['./exception.component.css']
})
export class ExceptionComponent implements OnInit {
  errorCode: number | undefined;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.errorCode = +!this.route.snapshot.paramMap.get('errorCode');
  }
}
