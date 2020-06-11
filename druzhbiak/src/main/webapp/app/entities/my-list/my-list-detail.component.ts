import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMyList } from 'app/shared/model/my-list.model';

@Component({
  selector: 'jhi-my-list-detail',
  templateUrl: './my-list-detail.component.html'
})
export class MyListDetailComponent implements OnInit {
  myList: IMyList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ myList }) => (this.myList = myList));
  }

  previousState(): void {
    window.history.back();
  }
}
